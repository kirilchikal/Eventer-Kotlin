# Eventer-Kotlin app
Event creation app for Android written in Kotlin with firebase. This application was created with the purpose of organizing parties and events 

### Compiling Environment
- compileSdkVersion 32
- targetSdkVersion 31
- Gradle Version 7.0.2
- Kotlin Version 1.6.10

### Setup Guide
You can use Android Studio or Intellij to work with this repository.
- Make sure you have the Android SDK installed
- Clone repository
- Add all the dependencies from [build.gradle](https://github.com/kirilchikal/Eventer-Kotlin/blob/master/build.gradle)
- Run application with Android simulator (min Android 11, x86 CPU)

### Desctop or Web version
The Web version of application is another [project]() written with React & ASP.NET Core

### Screen examples
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="254" height="117" viewBox="0 0 254 117">
  <g id="logo_wite" transform="translate(-1088 -1133)">
    <g id="Path_28" data-name="Path 28" transform="translate(1088 1133)" fill="#6b5dc1">
      <path d="M 253.5 116.5 L 0.5 116.5 L 0.5 0.5 L 253.5 0.5 L 253.5 116.5 Z" stroke="none"/>
      <path d="M 1 1 L 1 116 L 253 116 L 253 1 L 1 1 M 0 0 L 254 0 L 254 117 L 0 117 L 0 0 Z" stroke="none" fill="#707070"/>
    </g>
    <g id="LOGO" transform="translate(1115 1160)">
      <image id="balloons_1_" data-name="balloons (1)" width="64" height="64" xlink:href="data:image/png;base64,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"/>
      <text id="eventer" transform="translate(61 43)" fill="#fff" font-size="35" font-family="MVBoli, MV Boli" letter-spacing="0.05em"><tspan x="0" y="0">eventer</tspan></text>
    </g>
  </g>
</svg>


### Download a Xd template [here]()


