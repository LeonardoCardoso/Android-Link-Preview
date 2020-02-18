LeoCardz Link Preview for Android
=================================

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android--Link--Preview-green.svg?style=true)](https://android-arsenal.com/details/1/2755)

It makes a preview from an url, grabbing all the information such as title, relevant texts and images.

## Visual Examples
![Preview](images/VSejRyV.gif)

## Sample App
For a sample app, please install it from [Android Link Preview on Google Play](https://play.google.com/store/apps/details?id=com.leocardz.link.preview&feature=search_result "Android Link Preview on Google Play").


## Requirements
* [jsoup](http://jsoup.org/ "jsoup") is a smart lib to get the html code.


## Installation
### gradle

Simply add the repository to your build.gradle file:
```groovy
repositories {
	jcenter()
	maven { url 'https://github.com/leonardocardoso/mvn-repo/raw/master/maven-deploy' }
}
```

And you can use the artifacts like this:
```groovy
dependencies {
    compile 'org.jsoup:jsoup:1.8.3' // required
	compile 'com.leocardz:link-preview:2.0.0@aar'
	// ...
}
```

### ProGuard
If you use ProGuard, it is advised that you keep the jsoup dependencies  by adding 
```groovy
-keeppackagenames org.jsoup.nodes
```
to your ProGuard rules file.


## Usage
#### Instantiating 
```java
import com.leocardz.linkpreview.sample.library.TextCrawler;
// ...
// Create an instance of the TextCrawler to parse your url into a preview.
TextCrawler textCrawler = new TextCrawler();

// ..

// Create the callbacks to handle pre and post exicution of the preview generation.
LinkPreviewCallback linkPreviewCallback = new LinkPreviewCallback() {
    @Override
    public void onPre() {
        // Any work that needs to be done before generating the preview. Usually inflate 
        // your custom preview layout here.
    }

    @Override
    public void onPos(SourceContent sourceContent, boolean b) {
        // Populate your preview layout with the results of sourceContent.
    }
};
```

#### Generate Preview
```java
// using AsyncTask

textCrawler.makePreview(linkPreviewCallback, url);
```
```java
// using RxJava2
// no need to implement LinkPreviewCallback

textCrawler.makePreview(editText.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<SourceContent>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                linkPreviewDisposable = d;
                                linkPreviewCallback.onPre();
                            }

                            @Override
                            public void onNext(SourceContent sourceContent) {
                                try {
                                    linkPreviewCallback.onPos(sourceContent, !sourceContent.isSuccess());
                                } catch (Exception e) {
                                    onError(e);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
}
```

#### Cancel unfinished tasks when views are destroied.
If you are using Android Link Preview inside of an Activity, it is important to cancel unfinished Preview activites at the end of the Activity's lifecycle.

```java
// using AsyncTask

@Override
protected void onDestroy() {
    super.onDestroy();
    textCrawler.cancel();
}
```

```java
// using RxJava2

@Override
protected void onStop() {
    if (!linkPreviewDisposable.isDisposed()) linkPreviewDisposable.dispose();
    super.onStop();
}
```

Apps using Android Link Preview
=================================
1. [Unshorten It](https://play.google.com/store/apps/details?id=com.leocardz.url.unshortener&feature=search_result "Unshorten It")

2. ...


Information and Contact
===

Developed by [@LeonardoCardoso](https://github.com/LeonardoCardoso). 

Contact me either by Twitter [@leocardz](https://twitter.com/leocardz) or emailing me to [contact@leocardz.com](mailto:contact@leocardz.com).

Related Projects
===

* [Swift Link Preview](https://github.com/LeonardoCardoso/Swift-Link-Preview)
* [Link Preview (PHP + Angular + Bootstrap)](https://github.com/LeonardoCardoso/Link-Preview)

License
=================================

    Copyright 2013 Leonardo Cardoso

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
