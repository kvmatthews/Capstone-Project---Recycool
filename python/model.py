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
from tensorflow.keras.layers.experimental import preprocessing



# List available GPUs (optional)
gpus = tf.config.experimental.list_physical_devices('GPU')
if gpus:
    try:
        # Set memory growth for each GPU
        for gpu in gpus:
            tf.config.experimental.set_memory_growth(gpu, True)
    except RuntimeError as e:
        print(e)




dataset_dir = './RecycoolDataset'

batch_size = 32
image_size = (256,256)

train_ds = keras.utils.image_dataset_from_directory(
    directory=dataset_dir,
    label_mode='categorical',
    color_mode='rgb',
    batch_size=batch_size,
    image_size=image_size,
    #shuffle=False,
    validation_split=0.2,
    #interpolation='nearest',
    subset='training',
    seed=123
)

val_ds = keras.utils.image_dataset_from_directory(
    directory=dataset_dir,
    label_mode='categorical',
    color_mode='rgb',
    batch_size=batch_size,
    image_size=image_size,
    #interpolation='nearest',
    validation_split=0.2,
    subset='validation',
    seed=123
)


# Define a Sequential model for data augmentation
data_augmentation = Sequential([
    preprocessing.Rescaling(1./255),  # Rescale pixel values to [0,1]
    #preprocessing.RandomFlip("vertical"),  # Randomly flip images
    preprocessing.RandomRotation(0.2),
    preprocessing.RandomZoom(0.3),
    #preprocessing.RandomContrast(factor=0.2)
])
def preprocess_validation(x, y):
    # Rescale pixel values to [0,1]
    x = preprocessing.Rescaling(1./255)(x)
    return x, y


# Apply data augmentation to train_ds
train_ds = train_ds.map(lambda x, y: (data_augmentation(x), y))
val_ds = val_ds.map(preprocess_validation)
# Configure dataset for performance
AUTOTUNE = tf.data.AUTOTUNE
train_ds = train_ds.cache().prefetch(buffer_size=AUTOTUNE)
val_ds = val_ds.cache().prefetch(buffer_size=AUTOTUNE)




base_model = tf.keras.applications.mobilenet_v2.MobileNetV2(
            input_shape = (256,256, 3),
        include_top = False,
            weights="imagenet"
)
base_model.trainable = False

model = Sequential([
    base_model,
    GlobalMaxPooling2D(),
    Dense(256,activation='relu'),
    Dropout(0.5),
    Dense(5, activation='softmax')
])

model.compile(optimizer=tf.keras.optimizers.Adam(learning_rate=0.0001),
              loss=tf.keras.losses.categorical_crossentropy,
              metrics=['accuracy']
              )
model.summary()
##
import matplotlib.pyplot as plt
early_stopping = EarlyStopping(monitor='val_loss', patience=8)

# Train the model and collect history
history = model.fit(train_ds, epochs=30, validation_data=val_ds, callbacks=[early_stopping], verbose=1)

model.save('C:\\ANDRE\\Recycool-MultiClass-Model\\MulticlassRecycool.h5')



# Plot training & validation accuracy values
plt.plot(history.history['accuracy'])
plt.plot(history.history['val_accuracy'])
plt.title('Model accuracy')
plt.xlabel('Epoch')
plt.ylabel('Accuracy')
plt.legend(['Train', 'Validation'], loc='upper left')
plt.show()

# Plot training & validation loss values
plt.plot(history.history['loss'])
plt.plot(history.history['val_loss'])
plt.title('Model loss')
plt.xlabel('Epoch')
plt.ylabel('Loss')
plt.legend(['Train', 'Validation'], loc='upper left')
plt.show()

