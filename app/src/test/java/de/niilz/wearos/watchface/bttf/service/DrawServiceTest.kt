package de.niilz.wearos.watchface.bttf.service

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import de.niilz.wearos.watchface.bttf.drawable.DrawableItem
import de.niilz.wearos.watchface.bttf.drawable.DrawableLabel
import de.niilz.wearos.watchface.bttf.drawable.DrawableSlot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class DrawServiceTest {

  private val canvasInnerWidthOrHeight = 100f
  private val minLeftRightMaring = 5f

  @Test
  fun canCalculateTheEvenMargin() {
    // Given
    val mockContext = mock<Context>()
    val mockResources = mock<Resources>()
    whenever(mockResources.getColor(any(), any())).thenReturn(Color.BLUE)
    whenever(mockContext.resources).thenReturn(mockResources)

    val itemWidth = 10f
    val drawableItem = mock<DrawableItem>()
    whenever(drawableItem.getWidth()).thenReturn(itemWidth)
    val drawableItems = (1..4).map { drawableItem }
    val label = mock<DrawableLabel>()
    val slots =
      (1..3).map { DrawableSlot(mockContext, drawableItems, label, 0f, 0f, Color.BLACK) }
        .toMutableList()

    // 3 Slots times 4 items with times 10 itemwidth = 120 (one too large)
    val totalWidth = slots.map { slot -> slot.calcSlotWidth() }.sum()
    assertEquals(3, slots.size)
    assertEquals(120f, totalWidth)

    // When
    val evenMargin = DrawService.calculateEvenMargin(
      slots,
      canvasInnerWidthOrHeight,
      minLeftRightMaring
    );

    // Then
    // Once slot got removed
    assertEquals(2, slots.size)
    val newTotalWidth = slots.map { slot -> slot.calcSlotWidth() }.sum()
    assertEquals(80f, newTotalWidth)

    val expectedRemaining = canvasInnerWidthOrHeight - (2 * minLeftRightMaring) - 80 // 10
    val expectedEvenMargin = expectedRemaining / 2 // 5
    assertEquals(expectedEvenMargin, evenMargin)
  }
}