# colorPicker
高仿AndroidStudio 颜色选择器 以及其他自定义控件

UI WIDGET
======
# 使用
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


  	dependencies {
  	        implementation 'com.github.zhanpple:colorPicker:-SNAPSHOT'
  	}

```
* <H3>implementation 'com.github.zhanpple:colorPicker:-SNAPSHOT'</H3>

## 图片显示控件 [ShaderImageView源码](https://github.com/zhanpple/colorPicker/tree/master/widget/src/main/java/com/zmp/widget/shader/ShaderImageView.java)

### 属性
* <H4>     shape  rectangle 矩形 circle 圆形 star 星形 polygon 多边形
* <H4>     tilMode  clamp 延伸 repeat 平铺 mirror 镜像 fitXOY 放大铺满
* <H4>     padding 边距
* <H4>     radius 圆角 配合rectangle使用
* <H4>     src 图片资源
* <H4>     stroke_color 图片边框颜色 stroke_size 图片边框粗细
* <H4>     polygon_num 边数 配合star或者polygon使用

### 示例图

![示例图](https://github.com/zhanpple/colorPicker/tree/master/files/ui_pre.jpg "效果示例图")

### 用法
```xml

    <com.zmp.widget.shader.ShaderImageView
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_weight="1"
        app:padding="@dimen/dp20"
        app:polygon_num="3"
        app:radius="@dimen/dp5"
        app:shape="circle"
        app:src="@color/colorAccent"
        app:stroke_color="@color/colorPrimary"
        app:stroke_size="@dimen/dp5" />

    <com.zmp.widget.shader.ShaderImageView
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_weight="2"
        app:padding="@dimen/dp20"
        app:polygon_num="3"
        app:radius="@dimen/dp10"
        app:shape="polygon"
        app:src="@mipmap/ztimg4"
        app:stroke_color="@color/colorPrimary"
        app:stroke_size="@dimen/dp20" />

    <com.zmp.widget.shader.ShaderImageView
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_weight="3"
        app:padding="@dimen/dp20"
        app:polygon_num="5"
        app:radius="@dimen/dp10"
        app:shape="polygon"
        app:src="@mipmap/ztimg4"
        app:stroke_color="@color/colorAccent"
        app:stroke_size="@dimen/dp20" />

    <com.zmp.widget.shader.ShaderImageView
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_weight="4"
        app:padding="@dimen/dp20"
        app:polygon_num="6"
        app:radius="@dimen/dp20"
        app:shape="star"
        app:src="@mipmap/ztimg4"
        app:stroke_color="@color/colorYellow"
        app:stroke_size="@dimen/dp20" />

    <com.zmp.widget.shader.ShaderImageView
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_weight="5"
        app:padding="@dimen/dp20"
        app:radius="@dimen/dp20"
        app:shape="rectangle"
        app:src="@mipmap/ztimg4"
        app:stroke_color="@color/colorPrimary"
        app:stroke_size="@dimen/dp20" />

```
### 代码使用
```java
        shaderImageView.setDrawable(bitmap);
        shaderImageView.setBitmap(drawable);
        //Glide 使用
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.png";
        GlideApp.with(this)
                .load(absolutePath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher)
                .thumbnail(0.1f)
                .override(100, 100)
                .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                imageView4.setDrawable(resource);
                        }
                });
```

## 颜色选择控件 [ColorPickerView源码](https://github.com/zhanpple/colorPicker/tree/master/widget/src/main/java/com/zmp/widget/shader/ColorPickerView.java)
### 示例图
![示例图](https://github.com/zhanpple/colorPicker/tree/master/files/ui_pre2.jpg "效果示例图")
### 代码使用
```java
     //Dialog 方式
     ColorPickerDialog colorPickerDialog = new ColorPickerDialog(MainActivity.this);
     colorPickerDialog.setChooseListener(new ColorPickerDialog.IChooseListener() {
            @Override
            public void choose(int color) {
                    mainLL.setBackgroundColor(color);
            }
     });
     colorPickerDialog.show();

     //Activity方式   <activity android:name="com.zmp.widget.ui.ColorPickerActivity"/>
     startActivityForResult(new Intent(MainActivity.this, ColorPickerActivity.class), 100);

     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
             super.onActivityResult(requestCode, resultCode, data);
             if (resultCode == RESULT_OK && requestCode == 100) {
                     int color = data.getIntExtra(ColorPickerActivity.COLOR_KEY, Color.WHITE);
                     mainLL.setBackgroundColor(color);
             }
     }
```
### 示例图
![示例图](https://github.com/zhanpple/colorPicker/tree/master/files/ui_pre3s.png "效果示例图")
### 使用方法
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <com.zmp.widget.view.CircleChoiceView
        android:id="@+id/ccv"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />

    <com.zmp.widget.view.CircleProgressView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_margin="10dp"
        android:layout_weight="1"
        android:background="@color/colorYellow"
        app:cpv_centerBgColor="@color/colorP1"
        app:cpv_outBgColor="@color/colorGuideBlue"
        app:cpv_pointBgColor="@color/colorAccent"
        app:cpv_progress="80"
        app:cpv_progressBgColor="@color/colorBlue"
        app:cpv_progressColor="@color/colorPrimary"
        app:cpv_textColor="@color/colorWhite" />

    <com.zmp.widget.view.CircleProgressView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_margin="10dp"
        android:layout_weight="1"
        app:cpv_endMode="pointer"
        android:background="@color/colorP1"
        app:cpv_centerBgColor="@color/colorPrimary"
        app:cpv_outBgColor="@color/colorPrimaryDark"
        app:cpv_pointBgColor="@color/colorYellow"
        app:cpv_progress="80"
        app:cpv_progressBgColor="@color/colorBlue"
        app:cpv_progressColor="@color/colorRed"
        app:cpv_textColor="@color/colorWhite" />


    <com.zmp.widget.view.SportProgressView
        android:id="@+id/spv"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_margin="10dp"
        android:layout_weight="2"
        android:background="@color/colorP1"
        app:spv_centerBgColor="@color/colorGray"
        app:spv_max="100"
        app:spv_progress="80"
        app:spv_progressBgColor="@color/colorAccent"
        app:spv_maxAngle="240"
        app:spv_outBgColor="@color/colorBlack"
        app:spv_startAngle="150" />

    <com.zmp.widget.view.CircleProgressView
        android:id="@+id/cpv"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_margin="10dp"
        android:layout_weight="2"
        android:background="@color/colorP1"
        app:cpv_centerBgColor="@color/colorAccent"
        app:cpv_outBgColor="@color/colorYellow"
        app:cpv_pointBgColor="@color/colorPrimary"
        app:cpv_progress="80"
        app:cpv_endMode="pointer2"
        app:cpv_text="@string/app_name"
        app:cpv_progressBgColor="@color/colorGray"
        app:cpv_progressColor="@color/colorGuideBlue"
        app:cpv_startAngle="150"
        app:cpv_maxAngle="240"
        app:cpv_textColor="@color/colorWhite" />

    <com.zmp.widget.view.HProgressView
        android:id="@+id/hpv"
        android:layout_width="match_parent"
        android:layout_height="20dp"
        android:layout_margin="20dp"
        app:hp_progress="80"
        app:hp_progressBgColor="@color/colorAccent"
        app:hp_progressEndColor="#00FF00"
        app:hp_progressStartColor="#FF00FF" />


</LinearLayout>
```

## 有任何疑问或建议可随时联系邮箱: zhanpples@qq.com

