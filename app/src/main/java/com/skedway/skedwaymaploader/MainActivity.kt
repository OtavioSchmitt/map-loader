package com.skedway.skedwaymaploader

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.skedway.skedwaymaploader.ui.theme.SkedwayMapLoaderTheme
import io.github.sceneview.Scene
import io.github.sceneview.collision.HitResult
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkedwayMapLoaderTheme {
                MapLoader()
            }
        }
    }
}

@Composable
fun MapLoader() {
    val engine = rememberEngine()
    val view = rememberView(engine)
    val renderer = rememberRenderer(engine)
    val scene = rememberScene(engine)
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)
    val collisionSystem = rememberCollisionSystem(view)

    Scene(
        modifier = Modifier.fillMaxSize(),
        engine = engine,
        view = view,
        renderer = renderer,
        scene = scene,
        modelLoader = modelLoader,
        materialLoader = materialLoader,
        environmentLoader = environmentLoader,
        collisionSystem = collisionSystem,
        isOpaque = true,
        mainLightNode = rememberMainLightNode(engine) {
            intensity = 100_000.0f
        },
        cameraNode = rememberCameraNode(engine) {
            position = Position(z = 4.0f)
        },
        cameraManipulator = rememberCameraManipulator(),
        childNodes = rememberNodes {
            add(
                ModelNode(
                    modelInstance = modelLoader.createModelInstance(
                        assetFileLocation = "models/918.glb"
                    ),
                    scaleToUnits = 1.0f
                )
            )
        },
        onGestureListener = rememberOnGestureListener(
            onDoubleTapEvent = { event, tapedNode ->
                // Scale up the tap node (if any) on double tap
                tapedNode?.let { it.scale *= 2.0f }
            }),
        onTouchEvent = { event: MotionEvent, hitResult: HitResult? ->
            hitResult?.let { println("World tapped : ${it.worldPosition}") }
            false
        },
        onFrame = { frameTimeNanos -> }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SkedwayMapLoaderTheme {
        MapLoader()
    }
}