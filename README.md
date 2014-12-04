Json2ORM
========

Get data from REST API to your model, then save to/load from Sqlite database using annotation.

Annotations are in orm.annotation package. There are 3 kind of annotations:
  1. JSON annotation: which are used to transform JSON string to model object.
  2. serialize annotation: which are used to save model object to database.
  3. sql annotation: which are used to load data from database to model object.
We can combine these 3 kinds of annotation in 1 model class to archive all 3 capabilities.
  
Example for annotating model:
  1. JSON annotation: model.dataModel.AppConfigData
  2. serialize annotation: model.dataModel.RecipeCategoryData
  3. sql annotation: model.viewModel.BusinessCenterItem

Example for combining annotation:
  - model.dataModel.RecipeCategoryData: which can be parse from JSON string to model class, then save to db


Main classes:
  - orm.query.JsonRemoteDataGetter: parse JSON string into model object or model array
  - orm.query.ToDbSerializer: save model object to database
  - orm.query.DatabaseQuery: load data from database to model object

Example of using the above main classes:
  - MainActivity (beware: just example to demonstrate how to use the classes, we cant run as we need to create the database & load db & bla bla bla :-)
