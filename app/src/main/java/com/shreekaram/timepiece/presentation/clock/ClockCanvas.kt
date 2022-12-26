package com.shreekaram.timepiece.presentation.clock

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.drawscope.DrawContext
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.util.*


@Preview(showBackground = true)
@Composable
fun ClockCanvas() {
	val radius=300F
	val color= MaterialTheme.colors.onBackground
	val offset=Offset(0F,0F)

	var currentDate by remember{
		mutableStateOf(Date())
	}

	LaunchedEffect(key1 = currentDate ){
		delay(1000L)
		currentDate= Date()
	}

	val seconds = currentDate.seconds;
	val minute = currentDate.minutes;
	val hour = currentDate.hours % 12;

	val hRad=(Math.PI / 6) * hour + (Math.PI / 360) * minute + (Math.PI / 21600) * seconds

	val baseAngle=30F
//	hour hand moves 30deg
	val hourAngle=baseAngle*hour+(baseAngle*minute/60)+(baseAngle*seconds/3600)
//	minute hand moves 6 deg
	val minuteAngle=6F*minute+(6F*seconds/60)
//	seconds angle moves 6 deg
	val secondsAngle=seconds*6F

	val clockFacePaint=Paint().apply{
		this.color=Gray
		style = PaintingStyle.Stroke
		strokeWidth=10F
		strokeCap=StrokeCap.Round
	}
	val clockTickPaint=Paint().apply{
		this.color=Gray
		style = PaintingStyle.Stroke
		strokeWidth=2.5F
	}

	val clockNumbersPaint=Paint().asFrameworkPaint().apply{
		this.color=color.toArgb()
		this.textSize=40F
		this.strokeWidth=1F
		typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

	}
	val hourHandPaint=Paint().apply {
		this.style=PaintingStyle.Stroke
		this.strokeWidth=10F
		this.color= MaterialTheme.colors.primary
		this.strokeCap= StrokeCap.Round
	}
	val minuteHandPaint=Paint().apply {
		this.style=PaintingStyle.Stroke
		this.strokeWidth=6F
		this.color= color
		this.strokeCap= StrokeCap.Round
	}
	val secondsHandPaint=Paint().apply {
		this.style=PaintingStyle.Stroke
		this.strokeWidth=2F
		this.color= color
		this.strokeCap= StrokeCap.Round
	}

	Canvas(
		modifier = Modifier
			.padding(16.dp)
			.height(300.dp)
			.width(300.dp)

	){
//		translate canvas to center the origin
		drawContext.canvas.save()
		drawContext.canvas.translate(size.width/2,size.height/2)

//		clock face
		drawClockFace(
			drawContext=drawContext,
			radius=radius,
			paint = clockFacePaint
		)


//		clock ticks
		drawClockTicks(
			drawContext=drawContext,
			radius=	radius,
			paint=	clockTickPaint,
			offset=offset
		)

//		clock numbers
		drawClockNumbers(drawContext,radius,clockNumbersPaint,offset)

//		clock hour hand
		drawClockHand(
			drawContext,
			paint=hourHandPaint,
			angle=hourAngle,
			start= Offset(-16F,0F),
			end= Offset(120F,0F)
		)

//		clock minute hand
		drawClockHand(
			drawContext,
			paint=minuteHandPaint,
			angle=minuteAngle,
			start= Offset(-16F,0F),
			end= Offset(170F,0F)
		)

//		clock seconds hand
		drawClockHand(
			drawContext,
			paint=secondsHandPaint,
			angle=secondsAngle,
			start= Offset(-40F,0F),
			end= Offset(220F,0F)
		)

		drawCircle(
			radius = 12F,
			style = Fill,
			color=color,
			center= Offset(0F,2F)
		)

//		restore the canvas
		drawContext.canvas.restore()
	}
}

fun drawClockFace(drawContext:DrawContext,radius:Float,paint:Paint){
	 drawContext.canvas.drawCircle(
		radius = radius,
		center= Offset(0F,0F),
		paint=paint,
	)
}


fun drawClockTicks(drawContext: DrawContext,radius:Float,paint:Paint,offset:Offset){
	drawContext.canvas.save()
	for(i in 1..12){
		val path= Path()
		val angle=30F
		drawContext.canvas.rotate(angle,offset.x,offset.y);
		path.moveTo(radius-40,0F)
		path.lineTo(radius-20,0F)

		drawContext.canvas.drawPath(path=path,paint)
		path.close()
	}
	drawContext.canvas.restore()
}


fun drawClockNumbers(drawContext: DrawContext,radius:Float,paint:android.graphics.Paint, offset:Offset){
	val color=paint.color
	val typeface=paint.typeface
	drawContext.canvas.save()


	for(i in 1..12){
		val angle=i*30F
		drawContext.canvas.rotate(angle,offset.x,offset.y);
		drawContext.canvas.translate(0F, -radius*0.75F);
		drawContext.canvas.rotate(-angle,offset.x,offset.y);
		drawContext.canvas.nativeCanvas.drawText(
			i.toString(),
			if(i>9)-20F else -16F,
			16F,
			paint.apply {
				if (i%3==0){
					this.color= color
					this.strokeWidth=3F
					this.typeface=Typeface.create(Typeface.DEFAULT,Typeface.BOLD)
				}else{
					this.color=Gray.toArgb()
					this.typeface=typeface
				}
			}
		)
		drawContext.canvas.rotate(angle,offset.x,offset.y);
		drawContext.canvas.translate(0F, radius*0.75F)
		drawContext.canvas.rotate(-angle,offset.x,offset.y);
	}

	drawContext.canvas.restore()
}

fun drawClockHand(drawContext: DrawContext,paint:Paint,angle:Float,start:Offset,end:Offset){
	val path=Path()

	drawContext.canvas.save()

	drawContext.canvas.rotate(angle-90F)
	path.moveTo(start.x,start.y)
	path.lineTo(end.x,end.y)

	drawContext.canvas.drawPath(path=path, paint = paint)

	path.close()
	drawContext.canvas.restore()
}

