# waffle
Waffle core system

## 参考になるサイト

### MinChain

.NETで実装されていて、最小限の実装で書かれたブロックチェーン通貨システム。
スライド16ページのクラス図と似たものを作る予定。
- ソース: https://github.com/yutopio/MinChain  
- スライド: https://www.slideshare.net/YutoTakei/c-73041576

## Building Waffle

Gradle is a java building tool. To build, simply run

```bash
# for mac/linux/cygwin(?)
$ gradlew build
# for windows (haven't tried)
$ gradlew.bat build
```

To run,

```bash
$ gradlew run
```

OR expand zip file in `build/distriputions/waffle.zip` and run `waffle` or `waffle.bat`.
