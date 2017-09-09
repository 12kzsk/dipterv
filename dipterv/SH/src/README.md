# Shapelet Learner

This is a simple solution to generate shapelets based on a dataset.

Follow these instructions to generate shapelets:-
  - **DataExtractor** class can be used to partition and sample the dataset.(For the moment class can be only used on [**DEBS-2013-SoccerField**](http://www.orgs.ttu.edu/debs2013/index.php) dataset
  - Go to **tech.artisanhub.ARFFGenerator** package and set the input and output paths(Use the relevant class). This package is being used to generate the **ARFF** file.
  - Go tot **APP.java** class and set the **ARFFName** to the above generated **ARFF** file. Then set the output file name for serialized shapelets.
  - Run **APP.java** class.