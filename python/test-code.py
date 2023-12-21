## LIBRARY

import  os
import numpy as np
import tensorflow as tf
from tensorflow import keras
from keras.preprocessing.image import ImageDataGenerator
from keras.optimizers import Adam
from keras.layers import Dense, Flatten, Conv2D, MaxPooling2D, Activation, GlobalMaxPooling2D,Dropout,Input,Reshape
from keras import Sequential
from keras.models import Model
from keras.callbacks import Callback
from tensorflow.keras.callbacks import EarlyStopping
import  cv2
import imghdr
import matplotlib.pyplot as plt
import random
from tensorflow.keras.layers.experimental import preprocessing
from PIL import Image

test_dir ='./TEST'
image_paths =['O_713.jpg','R_310.jpg','R_1602.jpg','1.jpg','2.jpg','12.jpg','R_2467.jpg']

load_model = tf.keras.models.load_model('MulticlassRecycool.h5')

# Parameters
batch_size = 32
image_size = (256, 256)  # Replace with actual image dimensions used in training

# Create an ImageDataGenerator for test data
test_datagen = ImageDataGenerator(rescale=1./255)  # You can include other preprocessing steps if needed

# Generate batches of images and labels from the test data
test_generator = test_datagen.flow_from_directory(
    test_dir,
    target_size=image_size,
    batch_size=batch_size,
    class_mode='categorical',  # Change this based on your model's output
    shuffle=False  # To maintain the order of predictions
)

# Evaluate the model using the test data
evaluation = load_model.evaluate(test_generator)

# Print the evaluation metrics (e.g., accuracy, loss)
print(f"Test Loss: {evaluation[0]}")
print(f"Test Accuracy: {evaluation[1]}")

for image_path in image_paths:
    # Load the image
    image = Image.open(image_path)

    # Resize the image to match the dimensions used during training
    image = image.resize((256, 256))  # Replace with the actual dimensions used in training

    # Convert the image to an array and normalize it
    image_array = np.array(image) / 255.0  # Normalize the pixel values to [0, 1]

    # Reshape the image array according to the model's input shape
    image_array = np.expand_dims(image_array, axis=0)  # Add a batch dimension

    # Make a prediction using the loaded model
    prediction = load_model.predict(image_array)

    predicted_class_index = np.argmax(prediction, axis=1)[0]
    # Define class names (replace with your class names)
    class_names = ['Kaca', 'Kardus', 'Kertas', 'Organik', 'Plastik ']

    # Get the class name based on the predicted index
    predicted_class_name = class_names[predicted_class_index]

    # Get the class labels from the generator
    class_labels = list(test_generator.class_indices.keys())

    # Get the predicted class label and associated probability
    predicted_class = np.argmax(prediction, axis=1)[0]
    predicted_label = predicted_class_name
    confidence_score = prediction[0][predicted_class]  # Confidence score of the predicted class

    print(f"Image: {image_path}")
    print(f"Predicted Class: {predicted_label}")
    print(f"Confidence Score: {confidence_score:.4f}")  # Adjust the decimal places as needed
    print()