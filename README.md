# eye
eye (Easy Yang Editor) is a pure java GUI Yang Editor without any dependency of third party library
-- the file src/org/eye/gui/TextLineNumber.java is from internet. thanks for the original author.

features:
1. load existing yang files
2. edit/create yang file from tree view
3. edit/create yang file from text view
4. navigation between tree view and text view
5. basic validation (validate link only). It is suggest to use pyang for full validation

Note:
After edit yang file, please save it, then the yang file will be synced between tree view and text view

Usage:
    precondition:
        jre1.7 or above

    run:
        click on eye.jar for windows 
        java -jar eye.jar for linux 

Further developing:
    it is suggest to use eclipse to create a java project, and copy src folder to your project. The main function is implemeneted by EyeGui.java.
