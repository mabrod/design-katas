## CI Build
[![Build Status](https://travis-ci.org/mabrod/https://github.com/mabrod/design-katas.svg?branch=master)](https://travis-ci.org/mabrod/https://github.com/mabrod/design-katas)
I use Travis CI to run CI build pipeline: https://travis-ci.org/

## Chain of Responsibility Pattern

The code samples show how to design and implement Chain of Responsibility pattern to process a specific
file type. It uses [Apache Tika](https://tika.apache.org) library to determine media type of a file.

## Consumer-Producer
The code samples show how to quickly parse a huge input file (tested with 5 millions lines file) by encapsulating each 
line into an object record and push it into a shared queue (ArrayBlockingQueue) so a consumer can take it and decide what to do 
with each record. I have decided to filter only records with the top 5 highest web site access count. 
You might use this approach to parse a huge number of transactions from an
input file and see for example what the top 100 are. This approach avoids reading the whole data into memory to process it.

## Installation

The code is build with Gradle. Wrapper properties file to start a build with specific Gradle version is provided.

## Usage

I have provided samples of different file types to use with ChainResponsibilityRunner as a parameter.
I have provided a small websites_access.txt file to use with ConsumerProducerRunner as a parameter.

## Tests

Tests are run with Junit4 and Mockito framework.

## License

The code is free to use and modify.