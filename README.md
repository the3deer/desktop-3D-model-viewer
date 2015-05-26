Desktop 3DModel Viewer
======================

This is a demo of OpenGL.

It is basically a desktop application built on java + jogl (OpenGL bindings) that allows loading and viewing a 3D model,
manipulation and animation.

The purpose of this application was to learn the OpenGL language

Features
========

 - jogl powered (has native libraries)
 - obj format supported (wavefront)
 - displaying of normals
 - displaying of bounding box 
 - texture drawing
 - colors
 - scaling, rotation
 - object picking
 - primitive collision detection
 - mouse controls de world
 - moving of objects
 - animation of sprites
 
 
Try it
======

1 - Compile project to desktop-3DModel-viewer.jar
2 - Run with:
    java -jar desktop-3DModel-viewer.jar org.andresoviedo.app.modelviewer3D.Main -Djava.library.path="c:\path\to\desktop-3DModel-viewer\lib"
3- Load any obj from the models and textures from /models and /textures folders.


Final Notes
===========

You are free to use this program while you mantain this file and the authoring comments in the code.

Right now I'm not mantaining anymore this project. Anyway, comments and suggestions are welcome.