# Machine Learning Step by Step

## Collecting Data
gathering dataset needed for our recycool project that is needed for training and testing
### Data Sources
* Kaggle (https://www.kaggle.com/)
  
![Dataset](https://raw.githubusercontent.com/CH2-PS329-Recycool/Capstone-Project---Recycool/machine_learning/asset/dataset1.png)



## Preprocessing
preparation of raw data for machine learning. It involves cleaning, transforming, and augumenting data to optimize model performance
### Load & Split 
[![pipelining1](https://raw.githubusercontent.com/CH2-PS329-Recycool/Capstone-Project---Recycool/machine_learning/asset/data_pipelining1.png)](https://raw.githubusercontent.com/CH2-PS329-Recycool/Capstone-Project---Recycool/machine_learning/asset/data_pipelining1.png)

### Data Augumentation & Optimization
[![pipelining2](https://github.com/CH2-PS329-Recycool/Capstone-Project---Recycool/blob/machine_learning/asset/data_pipelining2.png)](https://github.com/CH2-PS329-Recycool/Capstone-Project---Recycool/blob/machine_learning/asset/data_pipelining2.png)



## Transfer Learning with MobileNetV2
Transfer learning with MobileNetV2 involves leveraging a pre-trained neural network model called MobileNetV2 for a new task. MobileNetV2 is a deep learning architecture known for its efficiency and effectiveness in computer vision tasks, particularly on mobile and embedded devices

### Training and saving model
[![transferlearning](https://github.com/CH2-PS329-Recycool/Capstone-Project---Recycool/blob/machine_learning/asset/transfer_learning.png)](https://github.com/CH2-PS329-Recycool/Capstone-Project---Recycool/blob/machine_learning/asset/transfer_learning.png)




## Testing
Testing phase aims to assess how well the trained model generalizes to new, unseen data and how accurately it makes predictions or classifications
in this phase we evaluate our model performance to classify unseen data. 

[![test](https://github.com/CH2-PS329-Recycool/Capstone-Project---Recycool/blob/machine_learning/asset/test_code.png)](https://github.com/CH2-PS329-Recycool/Capstone-Project---Recycool/blob/machine_learning/asset/test_code.png)



## Accuracy & Loss

### Model Accuracy
[![model_acc](https://github.com/CH2-PS329-Recycool/Capstone-Project---Recycool/blob/machine_learning/asset/model_acc.png)](https://github.com/CH2-PS329-Recycool/Capstone-Project---Recycool/blob/machine_learning/asset/model_acc.png)


### Model Loss
[![model_loss](https://github.com/CH2-PS329-Recycool/Capstone-Project---Recycool/blob/machine_learning/asset/model_loss.png)](https://github.com/CH2-PS329-Recycool/Capstone-Project---Recycool/blob/machine_learning/asset/model_loss.png)


### Testing Accuracy and Loss
[![test_acc](https://github.com/CH2-PS329-Recycool/Capstone-Project---Recycool/blob/machine_learning/asset/test_acc.png)](https://github.com/CH2-PS329-Recycool/Capstone-Project---Recycool/blob/machine_learning/asset/test_acc.png)


## Summarization
* The dataset was acquired from Kaggle
* The model encompasses five distinct classes: 1. Kaca 2. Karton 3. Kertas 4. Organik 5. Plastik
* Our model is constructed using MobileNetV2 transfer learning
* The model exhibits an accuracy of 95.12%, with a validation accuracy of 89.58%
* During the testing phase, our model achieved an accuracy of 82%




