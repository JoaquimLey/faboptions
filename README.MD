![AppIcon](../master/sample/src/main/res/mipmap-xxhdpi/ic_launcher.png) 

#FabOptions
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-FabOptions-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4734)
[![MaterialUp](https://img.shields.io/badge/MaterialUp-FabOptions-blue.svg?style=flat)](https://material.uplabs.com/posts/faboptions)
[![Bintray](https://img.shields.io/badge/Bintray-v1.0.0-brightgreen.svg?style=flat)](https://bintray.com/joaquimleyapps/opensource/com.github.joaquimley%3Afaboptions)
![minSdkVersion](https://img.shields.io/badge/minSdkVersion-21-red.svg?style=true)
![compileSdkVersion](https://img.shields.io/badge/compileSdkVersion-25-green.svg?style=true)

###A multi-functional FAB component with customizable options.
###Read more on the blog post: < WIP-Soon >

**Special thanks to [André Mion](https://github.com/andremion)** for the help provided on building this component.


Original concept by **Praveen Bisht** posted on [MaterialUp](https://www.uplabs.com/posts/options-floating-interaction), turned into code into open source library.


![Gif concept sample](../master/art/faboptions_materialup_demo.gif)

Android implementation

![Gif android sample](../master/art/faboptions_demo.gif)


##How to use
- Import gradle dependency:

		dependencies {
	 	   	compile 'com.github.joaquimley:faboptions:1.0.1'
		}
		

- Define a `menu` file with your buttons information **e.g.**

		<menu>
		    <item
		        android:id="@+id/faboptions_favorite"
		        android:icon="@drawable/ic_favorite"
		        android:title="Favorite" />
		
		    <item
		        android:id="@+id/faboptions_textsms"
		        android:icon="@drawable/ic_textsms"
		        android:title="Message" />
		
		    <item
		        android:id="@+id/faboptions_download"
		        android:icon="@drawable/ic_file_download"
		        android:title="Download" />
		
		
		    <item
		        android:id="@+id/faboptions_share"
		        android:icon="@drawable/ic_share"
		        android:title="Share" />
		</menu>
	


- Add the component to your layout:


		<com.joaquimley.faboptions.FabOptions
			android:id="@+id/fab_options"
	     	android:layout_width="wrap_content"
	   		android:layout_height="wrap_content"
	      	android:layout_gravity="bottom" />
	       

Via **XML**:

- Bind the buttons menu by adding the  **custom attribute** `app:button_menu="@menu/menu_your_menu"` to your component XML

**Programmatically**

- Bind the buttons menu on your FabOptions instance with FabOptions#setMenu()

        FabOptions fabOptions = (FabOptions) findViewById(R.id.fab_options);
        fabOptions.setButtonsMenu(this, R.menu.menu_your_menu);
       
       

**Listening for click events**

- Set your FabOptions instance click listener.

- Handle the click events for each button id defined on the `menu.xml`.


###The sample is also available on the Playstore

[![Get it on Google Play](../master/art/google-play-badge.png)](https://play.google.com/store/apps/details?id=com.joaquimley.faboptions.sample)

## Contributing

Contributions are always welcome!

**Issues:**
Fell free to open a new issue. Follow the [ISSUE_TEMPLATE.MD](../development/ISSUE_TEMPLATE.MD)

Follow the "fork-and-pull" Git workflow.

 1. **Fork** the repo on GitHub
 2. **Clone** the project to your own machine
 3. **Commit** changes to your own branch
 4. **Merge** with current *development* branch
 5. **Push** your work back up to your fork
 7. Submit a **Pull request** your changes can be reviewed (please refere the issue if reported)

**Prevent** code-style related changes (at least run Ctrl+⌥+O, ⌥+⌘+L) before commiting.


### License

	Copyright © 2016 Joaquim Ley

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
	implied.
	See the License for the specific language governing permissions and
	limitations under the License.
