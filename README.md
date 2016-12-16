react-native-sound-chat
=
	仿微信语音聊天，支持android,当前版本1.0.8。
1.功能
=
	1）android客户端仿微信语音的录制，停止录制，播放，停播，上传与下载。

	2）android客户端多图片同时上传。

	3）相关文件操作：删除本地录音文件，判断录音文件是否存在本地。

2.安装配置
=
安装npm包
-
	$ npm install --save react_native_sound_chat

添加权限Androidmanifast.xml
-
	<uses-permission android:name="android.permission.INTERNET" />
	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	<uses-permission android:name="android.permission.RECORD_AUDIO"/>
	<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
手动
-
1) android/app/src/main/包名/MainApplication.java

	import com.sound.chat.SoundPackage;//add this
	...
	@Override
    protected List<ReactPackage> getPackages() {
        return Arrays.<ReactPackage>asList(
            new MainReactPackage(),
            new SoundPackage()//add this
        );
    }
	...

2) android/setting.gradle

	include ':react_native_sound_chat'
	project(':react_native_sound_chat').projectDir = new File(rootProject.projectDir, '../node_modules/react_native_sound_chat/android')


3) android/app/build.gradle

	dependencies {
	  ...
   	  compile project(':react_native_sound_chat')//add this
	  ...
	}

3.方法及使用示例
=
引入
-

	import SoundModule from 'react_native_sound_chat';

initApi(uploadServerUrl)
-
参数：uploadServerUrl:上传服务器的地址。

功能：该方法必须在开始录制之前调用，除初始化上传地址之外还会初始化原生层环境。

示例：

	SoundModule.initApi(uploadServerUrl);
		
recordStart()
-
功能：开始录制，录制的音频文件格式为amr,会保存在本机redpack文件夹下。

示例：

	SoundModule.recordStart();
recordStop(callback)
-
参数：callback回调函数，返回字符串包含时长和本地存储路径，两者用逗号隔开。

功能：停止录制。

示例：

	var fileName,duration;//全局保存
	SoundModule.recordStop(function(data){
		console.log(data);
		var arr=data.split(',');
		duration=arr[0];//保存时长
		fileName=arr[1];//保存本地路径
	});
recordUpload()
-
功能：上传刚刚录制好的音频文件。

示例：
	
	SoundModule.recordUpload();

recordPlay(filePath)
-
参数：filePath:本地文件路径。

功能：播放音频。

示例：

	SoundModule.recordPlay('/storage/emulated/0/redpack/20161207_155930.amr');
recordStopPlay()
-
功能：停止播放。

示例：

	SoundModule.recordStopPlay();
fileDelete(filePath)
-
参数：filePath:本地文件路径。

功能：删除本地音频文件。

示例：

	SoundModule.fileDelete("/storage/emulated/0/redpack/20161207_155930.amr");
recordDownload(url)
-
参数：url:音频的url地址。

功能：音频文件下载。

示例：
	
	SoundModule.recordDownload("http://14.14.14.56:8888/amr/9ae1718c7b4eeec134a32a50.amr");

fileIsExits(filePath,callback)
-
参数:filePath:本地路径，callback:回调函数，返回布尔值，true表示存在。

功能：判断音频是否存在本地。

示例：
	
	SoundModule.fileIsExits('/storage/emulated/0/DCIM/Camera/1.jpg',function(data){
			console.log(data)//true||false
	})
	

SoundModule.imgsUpload(files,imgType);
-
参数：files:json数组，每一个json对象包含图片服务器地址imgServer和filePath本地文件路径两个参数，imgType:图片类型标志。

功能：上传图片组。

示例：

	var files=[
      {'imgServer':uploadServerUrl,'filePath':'/storage/emulated/0/DCIM/Camera/1.jpg'},
    ]
    SoundModule.imgsUpload(files,'chatImg');