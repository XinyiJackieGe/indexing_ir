# indexing_ir
> This web app uses tomcat servlet to upload a xml file of, build and display inverted indexes for it.

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Features](#features)
* [Status](#status)


## General info
It gets an xml file of paper records uploaded from user, and builds and display inverted indexes by zones. 

## Technologies
* tomcat - version 8.5 
* Java - version 8

## Setup
This app is a indexing.war file. Copy this file to tomcat/webapps/. It should be deployed automatically.
Start tomcat. In the browser, type "http://localhost:8080/indexing/". 
Upload an xml file and click Parse.

## Source Codes
Source codes are in indexing.zip file. 

## Features
List of features ready and TODOs for future development
* Builds inverted indexes by zones for quick query search.
* The document ids of a token is sorted.
* The sorted positions of a term in one document are stored in inverted index objects for later quick query search.


To-do list:
* Ranking.

## Status
Project is: _in progress_
