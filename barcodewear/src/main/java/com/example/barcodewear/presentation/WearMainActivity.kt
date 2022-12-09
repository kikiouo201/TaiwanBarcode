/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.barcodewear.presentation

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.ColorInt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.barcodewear.R
import com.example.barcodewear.presentation.theme.TaiwanBarcodeTheme
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code39Writer

class WearMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaiwanBarcodeTheme {
                val name = remember { mutableStateOf("12312312") }
                val barcodeText = remember { mutableStateOf("12312312") }

                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(20.dp)
                ) {
                    Greeting(name.value)
                    TextField(
                        value = name.value, onValueChange = {
                            name.value = it
                            Log.i("kk123", "1233 value = $name")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search, contentDescription = null
                            )
                        }, modifier = Modifier
                            .background(Color.LightGray)
                            .fillMaxWidth()
                            .heightIn(min = 56.dp)
                    )
//                    Button(onClick = {
//                        if (!TextUtils.isEmpty(name.value)
//                        ) {
//                            barcodeText.value = name.value
//                        }
//                    }) {
//                        Text(text = "add")
//                    }

                    displayBitmap(barcodeText.value)

                    Button(onClick = {
//                        com.google.android.apps.walletnfcrel
                        startNewActivity(this@WearMainActivity,"com.google.android.googlequicksearchbox")
                    }) {
                        Text(text = "add")
                    }
                }
            }
//            WearApp("Android")
        }
    }

    private fun startNewActivity(context: Context, packageName: String) {
        var intent: Intent? = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            Log.i("kk123", "intent = $intent")
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } else {
            // Bring user to the market or let them choose an app?
            intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("market://details?id=$packageName")
            Log.i("kk123", "ACTION_VIEW intent = $intent")
            context.startActivity(intent)
        }
    }

    @Composable
    private fun createBarcodeBitmap(
        barcodeValue: String,
        @ColorInt barcodeColor: Int,
        @ColorInt backgroundColor: Int,
        widthPixels: Int,
        heightPixels: Int
    ): ImageBitmap {
        val bitMatrix = Code39Writer().encode(
            barcodeValue,
            BarcodeFormat.CODE_39,
            widthPixels,
            heightPixels
        )

        val pixels = IntArray(bitMatrix.width * bitMatrix.height)
        for (y in 0 until bitMatrix.height) {
            val offset = y * bitMatrix.width
            for (x in 0 until bitMatrix.width) {
                pixels[offset + x] =
                    if (bitMatrix.get(x, y)) barcodeColor else backgroundColor
            }
        }

        val bitmap = Bitmap.createBitmap(
            bitMatrix.width,
            bitMatrix.height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(
            pixels,
            0,
            bitMatrix.width,
            0,
            0,
            bitMatrix.width,
            bitMatrix.height
        )
        return bitmap.asImageBitmap()
    }

    @Composable
    fun displayBitmap(value: String) {
        Image(
            bitmap = createBarcodeBitmap(
                barcodeValue = value,
                barcodeColor = getColor(android.R.color.black),
                backgroundColor = getColor(android.R.color.white),
                widthPixels = 1050,
                heightPixels = 350
            ), contentDescription = value
        )
    }
}

@Composable
fun WearApp(greetingName: String) {
    TaiwanBarcodeTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center
        ) {
            Greeting(greetingName = greetingName)
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = greetingName
//        stringResource(R.string.hello_world, greetingName)
    )
}

//@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}