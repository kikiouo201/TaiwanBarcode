package com.example.barcodewear.presentation

import androidx.wear.tiles.*
import androidx.wear.tiles.ColorBuilders.argb
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class BarcodeTileService : TileService() {

    private val RESOURCES_VERSION = "1"
    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<TileBuilders.Tile> =
        Futures.immediateFuture(
            TileBuilders.Tile.Builder()
                .setResourcesVersion(RESOURCES_VERSION)
                .setTimeline(
                    TimelineBuilders.Timeline.Builder().addTimelineEntry(
                        TimelineBuilders.TimelineEntry.Builder().setLayout(
                            LayoutElementBuilders.Layout.Builder().setRoot(
                                LayoutElementBuilders.Text.Builder().setText("Hello world!")
                                    .setFontStyle(
                                        LayoutElementBuilders.FontStyle.Builder()
                                            .setColor(argb(0xFFFFFF)).build()
                                    ).build()
                            ).build()
                        ).build()
                    ).build()
                ).build()
        )

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ListenableFuture<ResourceBuilders.Resources> =
        Futures.immediateFuture(
            ResourceBuilders.Resources.Builder()
                .setVersion(RESOURCES_VERSION)
                .build()
        )
}