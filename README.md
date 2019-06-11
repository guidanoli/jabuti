# Branch Setup Software

This is a project is intended to help the Visualization district set up and compile branches. Although it uses SVN commands and some internal routines such as 'vis s' and 'vis mlldamt', it is totally adjustable to whichever source control software you use on your personal or professional workspace. The language of choice will be Java in order to make use of the swing API, and pure OO programming.

## Runnable JAR

In order to export the program as an runnable JAR file, it is highly recommended the assistance of an IDE, like Eclipse, for example. There, you should first add the 'resources' folder to your Build Path. To do so, go to your project properties by right-clicking the folder icon of your project or by simply going to Project > Properties on the menu bar. There, go to Java Build Path and click on 'Add Folder...'. A pop up should show up, where you finally select 'resources' and press 'OK'. After applying these changes, right-click your project icon on the Project Explorer and click on 'Export...', open the folder 'Java' and select 'Runnable JAR file', click 'Next', select the output path to your JAR file. The other options may stay the same. Then, click on 'Finish'. For other IDEs, the process may be different, so you may consult its documentation.
