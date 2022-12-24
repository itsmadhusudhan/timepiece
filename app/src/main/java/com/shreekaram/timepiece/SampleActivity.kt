package com.shreekaram.timepiece

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shreekaram.timepiece.ui.theme.TimePieceTheme


data class Message(val author:String,val body:String)

@Composable
fun Conversation(messages: List<Message>){
    LazyColumn(modifier = Modifier.padding(bottom = 20.dp)){
        items(messages){ message->
            MessageCard(msg = message)
        }
    }
}

@Composable
fun MessageCard(msg: Message) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .clickable { isExpanded = !isExpanded }
            .padding(all=8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.madhusudhan),
            contentDescription =null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )

        Spacer(modifier = Modifier.width(4.dp))



//        val surfaceColor by animateColorAsState(if(isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface) ;

        Column(modifier = Modifier
            .fillMaxWidth()) {
            Text(
                text = msg.author,
                color= MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
//            elevation = 1.dp,
//            color = surfaceColor,
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all=4.dp),
                    style = MaterialTheme.typography.body2,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1
                )

            }


        }
    }
}


//@Preview(name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun DefaultPreview() {
    TimePieceTheme {
        Conversation(SampleData.conversationSample)
    }
}



//fun drawHourTicksPath(drawContext: DrawContext, center: Offset, color: Color) {
//	var paint= androidx.compose.ui.graphics.Paint()
//	var tpaint=android.graphics.Paint()
//
//	paint.color=color
//	paint.style = PaintingStyle.Stroke
//	paint.strokeWidth=3F
//
//	drawContext.canvas.save()
//	val radius=300F
//
//	for(i in 1..12){
//		var ipath=Path();
//
//		val x1=center.x+280
//		val x2=center.x+300
//		val angle=i*30F
//
//
////		drawContext.canvas.rotate(30F,center.x,center.y);
//
//		drawContext.canvas.rotate(angle,center.x,center.y);
//
////		drawContext.canvas.translate(0F, -radius*0.85F);
//
//		drawContext.canvas.rotate(-angle,center.x,center.y);
//		drawContext.canvas.nativeCanvas.drawText(
//			i.toString(),
//			x1-20,
//			center.y,
//			tpaint
//		)
////		ipath.moveTo(x1,center.y)
////		ipath.lineTo(x2,center.y)
////		ipath.moveTo(x1,center.y)
//
//		drawContext.canvas.rotate(angle,center.x,center.y);
////		drawContext.canvas.translate(0F, radius*0.85F)
//		drawContext.canvas.rotate(-angle,center.x,center.y);
//
//		drawContext.canvas.drawPath(ipath,paint)
//		ipath.close()
//
//	}
//
//	drawContext.canvas.restore()
//}
//
//
//
//fun drawHourHandPath(drawContext: DrawContext, center: Offset, color: Color) {
//	var paint= androidx.compose.ui.graphics.Paint()
//	val now=Date()
//
////	val hours=now.time.hours.toLong(DurationUnit.HOURS)
////	val minutes=now.time.minutes().toLong()
////	val seconds=now.time.seconds.toLong()
//
//	paint.color=color
//	paint.style = PaintingStyle.Stroke
//	paint.strokeWidth=4F
//	paint.strokeCap=StrokeCap.Round
//
//	drawContext.canvas.save()
//
//	var ipath=Path();
////	drawContext.canvas.rotate(30F,center.x,center.y);
//	ipath.moveTo(center.x-16,center.y)
//	ipath.lineTo(center.x+150,center.y)
//
//
//	drawContext.canvas.drawPath(ipath,paint)
//	ipath.close()
//
//	drawContext.canvas.drawCircle(
//		paint=paint.apply {
//			style=PaintingStyle.Fill
//		},
//		center=center,
//		radius = 10F
//	)
//
//
//	drawContext.canvas.restore()
//}
//
//fun drawMinuteHandPath(drawContext: DrawContext, center: Offset, color: Color) {
//	var paint= androidx.compose.ui.graphics.Paint()
//	val now=Date()
//
////	val hours=now.time.hours.toLong(DurationUnit.HOURS)
////	val minutes=now.time.minutes().toLong()
////	val seconds=now.time.seconds.toLong()
//
//	paint.color=color
//	paint.style = PaintingStyle.Stroke
//	paint.strokeWidth=4F
//	paint.strokeCap=StrokeCap.Round
//
//	drawContext.canvas.save()
//
//
//	var ipath=Path();
//	drawContext.canvas.rotate(30F,center.x,center.y);
//	ipath.moveTo(center.x-16,center.y)
//
//	ipath.lineTo(center.x+200,center.y)
//
//
//	drawContext.canvas.drawPath(ipath,paint)
//	ipath.close()
//
//	drawContext.canvas.restore()
//}


//@Preview(showBackground = true)
//@Composable
//fun ClockCanvas(){
//	val color=MaterialTheme.colors.onBackground
//	val radius=300F
//	val paint = android.graphics.Paint().apply {
//		textSize=30f
//	}
//	var currentDate by remember{
//		mutableStateOf(Date())
//	}
//
//	LaunchedEffect(key1 = currentDate ){
//		delay(1000L)
//		currentDate=Date()
//	}
//
//
//	val sec = currentDate.seconds;
//	val min = currentDate.minutes;
//	val hr = currentDate.hours % 12;
//
//	val hRad=(Math.PI / 6) * hr + (Math.PI / 360) * min + (Math.PI / 21600) * sec
//
//	val hAngle=(hRad* (180/Math.PI)).toFloat()
//	val mAngle=(((Math.PI / 30) * min + (Math.PI / 1800) * sec)*(180/Math.PI)).toFloat()
//	val sAngle=((sec * Math.PI) / 30)*180/Math.PI
//
//	Log.d("Time", hAngle.toString())
//	Log.d("Time", mAngle.toString())
//	Log.d("Time", sAngle.toString())
//
//	Canvas(
//		modifier = Modifier
//			.padding(16.dp)
//			.height(300.dp)
//			.width(300.dp)
//	){
//		drawContext.canvas.save()
//		drawContext.canvas.translate(size.width/2,size.height/2)
////		drawContext.canvas.rotate(-90F)
//
//
//		drawCircle(
//			radius = radius,
//			style = Stroke(width = 8f, cap = StrokeCap.Round),
//			color=color,
//			center= Offset(0F,0F)
//		)
//
//		drawCircle(
//			radius = 10F,
//			style = Fill,
//			color=color,
//			center= Offset(0F,2F)
//		)
//
//		val paint=Paint()
//		val tpaint=android.graphics.Paint()
//
//		paint.color=color
//		paint.style = PaintingStyle.Stroke
//		paint.strokeWidth=1.5F
//
//		val offset=Offset(0F,0F)
//		drawContext.canvas.save()
//		for(i in 1..12){
//			val path=Path()
//			val angle=30F
//			drawContext.canvas.rotate(angle,offset.x,offset.y);
//			path.moveTo(radius-40,0F)
//			path.lineTo(radius-20,0F)
//
//			drawContext.canvas.drawPath(path=path,paint)
//			path.close()
//		}
//		drawContext.canvas.restore()
//
//		drawContext.canvas.save()
//		for(i in 1..12){
//			val angle=i*30F
//
//			drawContext.canvas.rotate(angle,offset.x,offset.y);
//			drawContext.canvas.translate(0F, -radius*0.75F);
//			drawContext.canvas.rotate(-angle,offset.x,offset.y);
//			drawContext.canvas.nativeCanvas.drawText(
//				i.toString(),
//				if(i>9)-20F else -16F,
//				16F,
//				tpaint.apply {
//					this.color=color.toArgb()
//					this.textSize=40F
//					this.strokeWidth=1F
//				}
//			)
//			drawContext.canvas.rotate(angle,offset.x,offset.y);
//			drawContext.canvas.translate(0F, radius*0.75F)
//			drawContext.canvas.rotate(-angle,offset.x,offset.y);
//		}
//		drawContext.canvas.restore()
//
//
//		val hPath=Path()
//
////		hour
//		drawContext.canvas.save()
//		drawContext.canvas.rotate(hAngle-90F)
//		hPath.moveTo(-20F,0F)
//		hPath.lineTo(120F,0F)
//		drawContext.canvas.drawPath(
//			hPath,
//			paint.apply {
//				this.style=PaintingStyle.Stroke
//				this.strokeWidth=8F
//				this.color= Purple700
//				this.strokeCap= StrokeCap.Round
//			}
//		)
//		hPath.close()
//		drawContext.canvas.restore()
//
////		minute
//		drawContext.canvas.save()
//		drawContext.canvas.rotate(mAngle-90F)
//		val mPath=Path()
//		mPath.moveTo(-20F,0F)
//		mPath.lineTo(170F,0F)
//
//		drawContext.canvas.drawPath(
//			mPath,
//			paint.apply {
//				this.style=PaintingStyle.Stroke
//				this.strokeWidth=4F
//				this.color= color
//				this.strokeCap= StrokeCap.Round
//			}
//		)
//		mPath.close()
//		drawContext.canvas.restore()
//
//		// seconds
//		drawContext.canvas.save()
//		drawContext.canvas.rotate(sAngle.toFloat()-90F)
//		val sPath=Path()
//		sPath.moveTo(-20F,0F)
//		sPath.lineTo(170F,0F)
//
//		drawContext.canvas.drawPath(
//			sPath,
//			paint.apply {
//				this.style=PaintingStyle.Stroke
//				this.strokeWidth=4F
//				this.color= color
//				this.strokeCap= StrokeCap.Round
//			}
//		)
//		sPath.close()
//		drawContext.canvas.restore()
//
//
//
//		drawContext.canvas.restore()
//
//
//
//
////		val path= Path()
////
////			for(i in 1..2){
////				val angle=i*(30).toFloat()
//
////				drawContext.canvas.nativeCanvas.rotate(angle)
//
////				path.translate(Offset(0f,-radius))
//
////				drawContext.canvas.nativeCanvas.rotate(-angle)
////				drawContext.canvas.nativeCanvas.drawText(i.toString(), center.x + radius/2, center.y + radius/2, paint)
////				drawContext.canvas.nativeCanvas.rotate(angle)
////				path.translate(Offset(0f, radius));
////				drawContext.canvas.nativeCanvas.rotate(-angle)
//
////			}
//
////		drawPath(
////			path=path,
////			color = color
////			)
//
////		drawContext.canvas.nativeCanvas.restore()
//	}
//}
//
//

//class TimeModel: ViewModel() {
//    private var _date = MutableLiveData<Date>()
//
//    val date: LiveData<Date>
//        get()=_date
//
//    fun  setDate(value:Date){
//            _date.value=value
//    }
//}
//
//@Composable
//fun TimePiece(model:TimeModel=TimeModel()){
//    val sdf = SimpleDateFormat("hh:mm:ss")
//
//    var currentTime= model.date.observeAsState()
//
//    LaunchedEffect(key1 = currentTime.value ){
//        delay(1000L)
//        model.setDate(Date())
//    }
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//
//        modifier = Modifier
//            .fillMaxSize()
//            .fillMaxHeight()
//            .fillMaxWidth()
//            .padding(vertical = 20.dp, horizontal = 12.dp)
//
//    ) {
//
//        if(currentTime.value!=null)
//        Text(text = sdf.format(currentTime.value), style = MaterialTheme.typography.h2)
//
//    }
//}
//
