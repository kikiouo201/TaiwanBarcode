package com.example.barcodewear.presentation

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.wear.tiles.*
import androidx.wear.tiles.DimensionBuilders.dp
import androidx.wear.tiles.ModifiersBuilders.*
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.ResourceBuilders.IMAGE_FORMAT_UNDEFINED
import androidx.wear.tiles.ResourceBuilders.Resources
import androidx.wear.tiles.TimelineBuilders.Timeline
import androidx.wear.tiles.TimelineBuilders.TimelineEntry
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code39Writer
import java.io.ByteArrayOutputStream

class BarcodeTileService : TileService() {

    private val RESOURCES_VERSION = "1"

    override fun onTileRequest(requestParams: TileRequest): ListenableFuture<TileBuilders.Tile> =
        Futures.immediateFuture(
            TileBuilders.Tile.Builder()
                .setResourcesVersion(RESOURCES_VERSION)
                .setTimeline(
                    Timeline.Builder().addTimelineEntry(
                        TimelineEntry.Builder().setLayout(
                            LayoutElementBuilders.Layout.Builder().setRoot(
                                barcodeImage()
                            ).build()
                        ).build()
                    ).build()
                ).build()
        )

    override fun onResourcesRequest(requestParams: ResourcesRequest): ListenableFuture<Resources> =
        Futures.immediateFuture(
            Resources.Builder()
                .setVersion(RESOURCES_VERSION)
                .addIdToImageMapping(
                    "barcode_image", ResourceBuilders.ImageResource.Builder()
                        .setInlineResource(
                            ResourceBuilders.InlineImageResource.Builder()
                                .setData(
                                    createBarcodeByteArray(
                                        barcodeValue = "barcode",
                                        barcodeColor = Color.BLACK,
                                        backgroundColor = getColor(android.R.color.white),
                                        widthPixels = 1000,
                                        heightPixels = 300
                                    )
                                )
                                .setWidthPx(400)
                                .setHeightPx(150)
                                .setFormat(IMAGE_FORMAT_UNDEFINED)
                                .build()
                        )
                        .build()
                )
                .build()
        )

    private fun barcodeImage(): LayoutElementBuilders.LayoutElement =
        LayoutElementBuilders.Image.Builder()
            .setWidth(dp(200f))
            .setHeight(dp(150f))
            .setResourceId("barcode_image")
            .setModifiers(
                Modifiers.Builder()
                    .setPadding(Padding.Builder().setAll(dp(5f)).build())
                    .build()
            ).build()

    private fun createBarcodeByteArray(
        barcodeValue: String,
        @ColorInt barcodeColor: Int,
        @ColorInt backgroundColor: Int,
        widthPixels: Int,
        heightPixels: Int
    ): ByteArray {
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
        return imgBitmapToByteArray(bitmap)
    }

    private fun imgBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}