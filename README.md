# Recycool - Flask API
Using Flask to build an API for a machine learning image classification model. This API will then be deployed using App Engine.

## How to Use
### Running API from local server
 1. Create and activate the virtual environment
  ```
 python3 -m venv env
 source env/bin/activate
 ```
 2. Install the package requirements
 ```
 pip install -r requirements.txt
 ```
 3. Run with local server
 ```
 flask run
 ```
### Deployment
#### Enable Google App Engine Admin API
 1. In the left Navigation menu, click APIs & Services > Library.
 2. Type "App Engine Admin API" in the search box.
 3. Click the App Engine Admin API card.
 4. Click Enable.
#### Deploy using App Engine
 1. Setting up region, you can find your region here https://cloud.google.com/compute/docs/regions-zones?hl=id
```
gcloud config set compute/region "REGION"
```
 2. Deploy
```
gcloud app deploy
```



