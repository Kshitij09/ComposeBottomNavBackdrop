package com.kshitijpatil.compose.bottomnavbackdrop.components

import androidx.compose.animation.animate
import androidx.compose.animation.asDisposableClock
import androidx.compose.animation.core.AnimationClockObservable
import androidx.compose.animation.core.AnimationEndReason.Interrupted
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offsetPx
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.Saver
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.gesture.tapGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ExperimentalSubcomposeLayoutApi
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.zIndex
import com.kshitijpatil.compose.bottomnavbackdrop.components.BackdropValue.Concealed
import com.kshitijpatil.compose.bottomnavbackdrop.components.BackdropValue.Revealed
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Possible values of [BackdropScaffoldState].
 */
enum class BackdropValue {
    /**
     * Indicates the back layer is concealed and the front layer is active.
     */
    Concealed,

    /**
     * Indicates the back layer is revealed and the front layer is inactive.
     */
    Revealed
}

/**
 * State of the [HorizontalBackdropScaffold] composable.
 *
 * @param initialValue The initial value of the state.
 * @param clock The animation clock that will be used to drive the animations.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 * @param snackbarHostState The [SnackbarHostState] used to show snackbars inside the scaffold.
 */
@ExperimentalMaterialApi
@Stable
class BackdropScaffoldState(
    initialValue: BackdropValue,
    clock: AnimationClockObservable,
    animationSpec: AnimationSpec<Float> = SwipeableConstants.DefaultAnimationSpec,
    confirmStateChange: (BackdropValue) -> Boolean = { true },
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
) : SwipeableState<BackdropValue>(
    initialValue = initialValue,
    clock = clock,
    animationSpec = animationSpec,
    confirmStateChange = confirmStateChange
) {
    /**
     * Whether the back layer is revealed.
     */
    val isRevealed: Boolean
        get() = value == Revealed

    /**
     * Whether the back layer is concealed.
     */
    val isConcealed: Boolean
        get() = value == Concealed

    /**
     * Reveal the back layer, with an animation.
     *
     * @param onRevealed Optional callback invoked when the back layer has been revealed.
     */
    fun reveal(onRevealed: (() -> Unit)? = null) {
        animateTo(
            targetValue = Revealed,
            onEnd = { endReason, endValue ->
                if (endReason != Interrupted && endValue == Revealed) {
                    onRevealed?.invoke()
                }
            }
        )
    }

    /**
     * Conceal the back layer, with an animation.
     *
     * @param onConcealed Optional callback invoked when the back layer has been concealed.
     */
    fun conceal(onConcealed: (() -> Unit)? = null) {
        animateTo(
            targetValue = Concealed,
            onEnd = { endReason, endValue ->
                if (endReason != Interrupted && endValue == Concealed) {
                    onConcealed?.invoke()
                }
            }
        )
    }

    companion object {
        /**
         * The default [Saver] implementation for [BackdropScaffoldState].
         */
        fun Saver(
            clock: AnimationClockObservable,
            animationSpec: AnimationSpec<Float>,
            confirmStateChange: (BackdropValue) -> Boolean,
            snackbarHostState: SnackbarHostState
        ): Saver<BackdropScaffoldState, *> = Saver(
            save = { it.value },
            restore = {
                BackdropScaffoldState(
                    initialValue = it,
                    clock = clock,
                    animationSpec = animationSpec,
                    confirmStateChange = confirmStateChange,
                    snackbarHostState = snackbarHostState
                )
            }
        )
    }
}

/**
 * Create and [remember] a [BackdropScaffoldState] with the default animation clock.
 *
 * @param initialValue The initial value of the state.
 * @param clock The animation clock that will be used to drive the animations.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 * @param snackbarHostState The [SnackbarHostState] used to show snackbars inside the scaffold.
 */
@Composable
@ExperimentalMaterialApi
fun rememberBackdropScaffoldState(
    initialValue: BackdropValue,
    clock: AnimationClockObservable = AnimationClockAmbient.current,
    animationSpec: AnimationSpec<Float> = SwipeableConstants.DefaultAnimationSpec,
    confirmStateChange: (BackdropValue) -> Boolean = { true },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): BackdropScaffoldState {
    val disposableClock = clock.asDisposableClock()
    return rememberSavedInstanceState(
        disposableClock,
        animationSpec,
        confirmStateChange,
        snackbarHostState,
        saver = BackdropScaffoldState.Saver(
            clock = disposableClock,
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange,
            snackbarHostState = snackbarHostState
        )
    ) {
        BackdropScaffoldState(
            initialValue = initialValue,
            clock = disposableClock,
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange,
            snackbarHostState = snackbarHostState
        )
    }
}

/**
 * A backdrop appears behind all other surfaces in an app, displaying contextual and actionable
 * content. It is composed of two surfaces: a back layer and a front layer. The back layer
 * displays actions and context, and these control and inform the front layer's content.
 *
 * This component provides an API to put together several material components to construct your
 * screen. For a similar component which implements the basic material design layout strategy
 * with app bars, floating action buttons and navigation drawers, use the standard [Scaffold].
 * For similar component that uses a bottom sheet as the centerpiece of the screen, use
 * [BottomSheetScaffold].
 *
 * Either the back layer or front layer can be active at a time. When the front layer is active,
 * it sits at an offset below the top of the screen. This is the [peekWidth] and defaults to
 * 56dp which is the default app bar height. When the front layer is inactive, it sticks to the
 * height of the back layer's content if [stickyFrontLayer] is set to `true` and the height of
 * the front layer exceeds the [headerWidth], and otherwise it minimizes to the [headerWidth].
 * To switch between the back layer and front layer, you can either swipe on the front layer if
 * [gesturesEnabled] is set to `true` or use any of the methods in [BackdropScaffoldState].
 *
 * The scaffold also contains an app bar, which by default is placed above the back layer's
 * content. If [persistentAppBar] is set to `false`, then the backdrop will not show the app bar
 * when the back layer is revealed; instead it will switch between the app bar and the provided
 * content with an animation. For best results, the [peekWidth] should match the app bar height.
 * To show a snackbar, use the method `showSnackbar` of [BackdropScaffoldState.snackbarHostState].
 *
 * A simple example of a backdrop scaffold looks like this:
 *
 * @sample androidx.compose.material.samples.BackdropScaffoldSample
 *
 * @param modifier Optional [Modifier] for the root of the scaffold.
 * @param scaffoldState The state of the scaffold.
 * @param gesturesEnabled Whether or not the backdrop can be interacted with by gestures.
 * @param peekWidth The height of the visible part of the back layer when it is concealed.
 * @param headerWidth The minimum height of the front layer when it is inactive.
 * @param persistentAppBar Whether the app bar should be shown when the back layer is revealed.
 * By default, it will always be shown above the back layer's content. If this is set to `false`,
 * the back layer will automatically switch between the app bar and its content with an animation.
 * @param stickyFrontLayer Whether the front layer should stick to the height of the back layer.
 * @param backLayerBackgroundColor The background color of the back layer.
 * @param backLayerContentColor The preferred content color provided by the back layer to its
 * children. Defaults to the matching `onFoo` color for [backLayerBackgroundColor], or if that
 * is not a color from the theme, this will keep the same content color set above the back layer.
 * @param frontLayerShape The shape of the front layer.
 * @param frontLayerElevation The elevation of the front layer.
 * @param frontLayerBackgroundColor The background color of the front layer.
 * @param frontLayerContentColor The preferred content color provided by the back front to its
 * children. Defaults to the matching `onFoo` color for [frontLayerBackgroundColor], or if that
 * is not a color from the theme, this will keep the same content color set above the front layer.
 * @param frontLayerScrimColor The color of the scrim applied to the front layer when the back
 * layer is revealed. If you set this to `Color.Transparent`, then a scrim will not be applied
 * and interaction with the front layer will not be blocked when the back layer is revealed.
 * @param snackbarHost The component hosting the snackbars shown inside the scaffold.
 * @param appBar App bar for the back layer. Make sure that the [peekWidth] is equal to the
 * height of the app bar, so that the app bar is fully visible. Consider using [TopAppBar] but
 * set the elevation to 0dp and background color to transparent as a surface is already provided.
 * @param backLayerContent The content of the back layer.
 * @param frontLayerContent The content of the front layer.
 */
@Composable
@ExperimentalMaterialApi
fun HorizontalBackdropScaffold(
    modifier: Modifier = Modifier,
    scaffoldState: BackdropScaffoldState = rememberBackdropScaffoldState(Concealed),
    gesturesEnabled: Boolean = true,
    peekWidth: Dp = HorizontalBackdropScaffoldConstants.DefaultPeekWidth,
    headerWidth: Dp = HorizontalBackdropScaffoldConstants.DefaultHeaderWidth,
    stickyFrontLayer: Boolean = true,
    backLayerBackgroundColor: Color = MaterialTheme.colors.primary,
    backLayerContentColor: Color = contentColorFor(backLayerBackgroundColor),
    frontLayerShape: Shape = HorizontalBackdropScaffoldConstants.DefaultFrontLayerShape,
    frontLayerElevation: Dp = HorizontalBackdropScaffoldConstants.DefaultFrontLayerElevation,
    frontLayerBackgroundColor: Color = MaterialTheme.colors.surface,
    frontLayerContentColor: Color = contentColorFor(frontLayerBackgroundColor),
    frontLayerScrimColor: Color = HorizontalBackdropScaffoldConstants.DefaultFrontLayerScrimColor,
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    backLayerContent: @Composable () -> Unit,
    frontLayerContent: @Composable () -> Unit
) {
    val peekWidthPx = with(DensityAmbient.current) { peekWidth.toPx() }
    val headerWidthPx = with(DensityAmbient.current) { headerWidth.toPx() }

    val backLayer = @Composable {
        BackLayerTransition(scaffoldState.targetValue, backLayerContent)
    }
    val calculateBackLayerConstraints: (Constraints) -> Constraints = {
        it.copy(minWidth = 0, minHeight = 0).offset(horizontal = -headerWidthPx.roundToInt())
    }

    // Back layer
    Surface(
        color = backLayerBackgroundColor,
        contentColor = backLayerContentColor
    ) {
        BackdropStack(
            modifier.fillMaxSize(),
            backLayer,
            calculateBackLayerConstraints
        ) { constraints, backLayerWidth ->
            val fullWidth = constraints.maxWidth.toFloat()
            var revealedWidth = fullWidth - headerWidthPx
            if (stickyFrontLayer) {
                revealedWidth = min(revealedWidth, backLayerWidth)
            }

            val swipeable = Modifier.swipeable(
                state = scaffoldState,
                anchors = mapOf(
                    peekWidthPx to Concealed,
                    revealedWidth to Revealed
                ),
                orientation = Orientation.Horizontal,
                enabled = gesturesEnabled
            )

            // Front layer
            Surface(
                Modifier.offsetPx(x = scaffoldState.offset).then(swipeable),
                shape = frontLayerShape,
                elevation = frontLayerElevation,
                color = frontLayerBackgroundColor,
                contentColor = frontLayerContentColor
            ) {
                Box(Modifier.padding(end = peekWidth)) {
                    frontLayerContent()
                    Scrim(
                        color = frontLayerScrimColor,
                        onDismiss = { scaffoldState.conceal() },
                        visible = scaffoldState.targetValue == Revealed
                    )
                }
            }

            // Snackbar host
            Box(
                Modifier
                    .zIndex(Float.POSITIVE_INFINITY)
                    .padding(
                        bottom = if (scaffoldState.isRevealed &&
                            revealedWidth == fullWidth - headerWidthPx
                        ) headerWidth else 0.dp
                    ),
                alignment = Alignment.BottomCenter
            ) {
                snackbarHost(scaffoldState.snackbarHostState)
            }
        }
    }
}

@Composable
private fun Scrim(
    color: Color,
    onDismiss: () -> Unit,
    visible: Boolean
) {
    if (color != Color.Transparent) {
        val alpha = animate(target = if (visible) 1f else 0f, animSpec = TweenSpec())
        val dismissModifier = if (visible) Modifier.tapGestureFilter { onDismiss() } else Modifier

        Canvas(
            Modifier
                .fillMaxSize()
                .then(dismissModifier)
        ) {
            drawRect(color = color, alpha = alpha)
        }
    }
}

/**
 * A shared axis transition, used in the back layer. Both the [appBar] and the [content] shift
 * vertically, while they crossfade. It is very important that both are composed and measured,
 * even if invisible, and that this component is as large as both of them.
 */
@Composable
private fun BackLayerTransition(
    target: BackdropValue,
    content: @Composable () -> Unit
) {
    // The progress of the animation between Revealed (0) and Concealed (2).
    // The midpoint (1) is the point where the appBar and backContent are switched.
    val animationProgress = animate(
        target = if (target == Revealed) 0f else 2f, animSpec = TweenSpec()
    )
    val animationSlideOffset = with(DensityAmbient.current) { AnimationSlideOffset.toPx() }

    val contentFloat = (1 - animationProgress).coerceIn(0f, 1f)

    Box {
        Box(
            Modifier.zIndex(contentFloat).drawLayer(
                alpha = contentFloat,
                translationX = (1 - contentFloat) * -animationSlideOffset
            )
        ) {
            content()
        }
    }
}

@Composable
@OptIn(ExperimentalSubcomposeLayoutApi::class)
private fun BackdropStack(
    modifier: Modifier,
    backLayer: @Composable () -> Unit,
    calculateBackLayerConstraints: (Constraints) -> Constraints,
    frontLayer: @Composable (Constraints, Float) -> Unit
) {
    SubcomposeLayout<BackdropLayers>(modifier) { constraints ->
        val backLayerPlaceable =
            subcompose(BackdropLayers.Back, backLayer).first()
                .measure(calculateBackLayerConstraints(constraints))

        val backLayerWidth = backLayerPlaceable.width.toFloat()

        val placeables =
            subcompose(BackdropLayers.Front) {
                frontLayer(constraints, backLayerWidth)
            }.fastMap { it.measure(constraints) }

        var maxWidth = max(constraints.minWidth, backLayerPlaceable.width)
        var maxHeight = max(constraints.minHeight, backLayerPlaceable.height)
        placeables.fastForEach {
            maxWidth = max(maxWidth, it.width)
            maxHeight = max(maxHeight, it.height)
        }

        layout(maxWidth, maxHeight) {
            backLayerPlaceable.placeRelative(0, 0)
            placeables.fastForEach { it.placeRelative(0, 0) }
        }
    }
}

private enum class BackdropLayers { Back, Front }

/**
 * Contains useful constants for [HorizontalBackdropScaffold].
 */
object HorizontalBackdropScaffoldConstants {

    /**
     * The default peek height of the back layer.
     */
    val DefaultPeekWidth = 72.dp

    /**
     * The default header height of the front layer.
     */
    val DefaultHeaderWidth = 60.dp

    /**
     * The default shape of the front layer.
     */
    @Composable
    val DefaultFrontLayerShape: Shape
        get() = MaterialTheme.shapes.large
            .copy(topLeft = CornerSize(16.dp), bottomLeft = CornerSize(16.dp))

    /**
     * The default elevation of the front layer.
     */
    val DefaultFrontLayerElevation = 2.dp

    /**
     * The default color of the scrim applied to the front layer.
     */
    @Composable
    val DefaultFrontLayerScrimColor: Color
        get() = MaterialTheme.colors.surface.copy(alpha = 0.60f)
}

private val AnimationSlideOffset = 20.dp