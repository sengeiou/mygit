## 项目说明
图片选择器，选择第三方控件YancyYe的[ImageSelector](https://github.com/YancyYe/ImageSelector)

其他的选择还有
[ImagePicker](https://github.com/jeasonlzy/ImagePicker)
[ImageSelector](https://github.com/smuyyh/ImageSelector)
[PhotoPicker](https://github.com/donglua/PhotoPicker)


## 使用说明

### 步骤一：

#### 通过Gradle抓取

```groovy
dependencies {
    compile project(':l2_imageselector')
}
```


### 步骤二：

在 `AndroidManifest.xml` 中 添加 如下权限

```xml
<!-- 从sdcard中读取数据的权限 -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<!-- 往sdcard中写入数据的权限 -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

```

### 步骤三：

#### 配置 `ImageConfig`

##### UI 视图配置

```java
 ImageConfig imageConfig
      = new ImageConfig.Builder(new GlideLoader())
     // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
     .steepToolBarColor(getResources().getColor(R.color.blue))
     // 标题的背景颜色 （默认黑色）
     .titleBgColor(getResources().getColor(R.color.blue))
     // 提交按钮字体的颜色  （默认白色）
     .titleSubmitTextColor(getResources().getColor(R.color.white))
     // 标题颜色 （默认白色）
     .titleTextColor(getResources().getColor(R.color.white))
     .build();
```

##### 多选
```java
 ImageConfig imageConfig
        = new ImageConfig.Builder(new GlideLoader())
        .steepToolBarColor(getResources().getColor(R.color.blue))
        .titleBgColor(getResources().getColor(R.color.blue))
        .titleSubmitTextColor(getResources().getColor(R.color.white))
        .titleTextColor(getResources().getColor(R.color.white))
        // 开启多选   （默认为多选）
        .mutiSelect()
        // 多选时的最大数量   （默认 9 张）
        .mutiSelectMaxSize(9)
        // 开启拍照功能 （默认关闭）
        .showCamera()
        // 已选择的图片路径
        .pathList(path)
        // 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
        .filePath("/ImageSelector/Pictures")
        .build();


ImageSelector.open(MainActivity.this, imageConfig);   // 开启图片选择器
```

##### 单选
```java
 ImageConfig imageConfig
        = new ImageConfig.Builder(new GlideLoader())
        .steepToolBarColor(getResources().getColor(R.color.blue))
        .titleBgColor(getResources().getColor(R.color.blue))
        .titleSubmitTextColor(getResources().getColor(R.color.white))
        .titleTextColor(getResources().getColor(R.color.white))
        // 开启单选   （默认为多选）
        .singleSelect()
        // 开启拍照功能 （默认关闭）
        .showCamera()
        // 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
        .filePath("/ImageSelector/Pictures")
        .build();


ImageSelector.open(MainActivity.this, imageConfig);   // 开启图片选择器
```

##### 单选1：1 便捷截图
```java
 ImageConfig imageConfig
        = new ImageConfig.Builder(new GlideLoader())
        .steepToolBarColor(getResources().getColor(R.color.blue))
        .titleBgColor(getResources().getColor(R.color.blue))
        .titleSubmitTextColor(getResources().getColor(R.color.white))
        .titleTextColor(getResources().getColor(R.color.white))
        // (截图默认配置：关闭    比例 1：1    输出分辨率  500*500)
        .crop()
        // 开启单选   （默认为多选）
        .singleSelect()
        // 开启拍照功能 （默认关闭）
        .showCamera()
        // 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
        .filePath("/ImageSelector/Pictures")
        .build();


ImageSelector.open(MainActivity.this, imageConfig);   // 开启图片选择器
```

##### 单选自定义截图
```java
 ImageConfig imageConfig
        = new ImageConfig.Builder(new GlideLoader())
        .steepToolBarColor(getResources().getColor(R.color.blue))
        .titleBgColor(getResources().getColor(R.color.blue))
        .titleSubmitTextColor(getResources().getColor(R.color.white))
        .titleTextColor(getResources().getColor(R.color.white))
        // (截图默认配置：关闭    比例 1：1    输出分辨率  500*500)
        .crop(1, 2, 500, 1000)
        // 开启单选   （默认为多选）
        .singleSelect()
        // 开启拍照功能 （默认关闭）
        .showCamera()
        // 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
        .filePath("/ImageSelector/Pictures")
        .build();


ImageSelector.open(MainActivity.this, imageConfig);   // 开启图片选择器
```
### 步骤四：

在  `onActivityResult` 中获取选中的照片路径 数组 :

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 super.onActivityResult(requestCode, resultCode, data);
  if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

    // Get Image Path List
     List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

     for (String path : pathList) {
         Log.i("ImagePathList", path);
     }
  }
}
```
