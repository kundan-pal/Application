from flask import Flask, jsonify, request
import base64
from PIL import Image
import os

# declaring a dictionary for objects corresponding to their indices
labels = {0: 'person', 1: 'bicycle', 2: 'car', 3: 'motorcycle', 4: 'airplane', 5: 'bus', 6: 'train', 7: 'truck', 8: 'boat', 9: 'traffic', 10: 'fire', 11: 'stop', 12: 'parking', 13: 'bench', 14: 'bird', 15: 'cat', 16: 'dog', 17: 'horse', 18: 'sheep', 19: 'cow', 20: 'elephant', 21: 'bear', 22: 'zebra', 23: 'giraffe', 24: 'backpack', 25: 'umbrella', 26: 'handbag', 27: 'tie', 28: 'suitcase', 29: 'frisbee', 30: 'skis', 31: 'snowboard', 32: 'sports', 33: 'kite', 34: 'baseball', 35: 'baseball', 36: 'skateboard', 37: 'surfboard', 38: 'tennis', 39: 'bottle', 40: 'wine', 41: 'cup', 42: 'fork', 43: 'knife', 44: 'spoon', 45: 'bowl', 46: 'banana', 47: 'apple', 48: 'sandwich', 49: 'orange', 50: 'broccoli', 51: 'carrot', 52: 'hot', 53: 'pizza', 54: 'donut', 55: 'cake', 56: 'chair', 57: 'couch', 58: 'potted', 59: 'bed', 60: 'dining', 61: 'toilet', 62: 'tv', 63: 'laptop', 64: 'mouse', 65: 'remote', 66: 'keyboard', 67: 'cell', 68: 'microwave', 69: 'oven', 70: 'toaster', 71: 'sink', 72: 'refrigerator', 73: 'book', 74: 'clock', 75: 'vase', 76: 'scissors', 77: 'teddy', 78: 'hair', 79: 'toothbrush'}
app = Flask(__name__)

# declaring type of request this server is supposed to handle : get and post
@app.route('/', methods=['GET', 'POST'])
def handle_request():
    # get request for fetching data from server
    if request.method == 'GET':
        return jsonify("Flask Server is Running Successfully !!!")

    # post request for sending data to the server and getting back response
    if request.method == 'POST':
        data =  request.data
        print(len(data))
        image = base64.b64decode(data)      # converting base64 string to image
        filename = "yolov5-master/image.jpg"
        with open(filename, 'wb') as f:     # storing image file into the server
            f.write(image)
        
        image = Image.open('yolov5-master/image.jpg')       # opening image for confimation that image capture by phones camera has been delivered to server

        # executing terminal command using os library for yolo model processing on the image taken and storin results 
        os.system("cd yolov5-master && python detect.py --weights yolov5s.pt --img 640 --conf 0.25 --source image.jpg --save-txt --save-conf")
        
        # declaring the path where results are being stored
        dir_list = os.listdir("yolov5-master/runs/detect")
        l2 = sorted(dir_list, key=len)
        # most recent experiment which was done by yolo modle processing
        exp = l2[-1]

        path = "yolov5-master/runs/detect/" + exp + "/labels/image.txt"
        # print(path)
        result = ""
        if os.path.exists(path):
            file = open(path, 'r') 
            lines = file.readlines()
            ids = []
            for x in lines:
                ids.append(int(x.split(" ")[0]))

            for x in ids:
                result = result +" "+ labels[x]
            print(result)
        
        
        print(len(data))
        # print(result) 
        image.show()
        return jsonify(result)


if __name__=='__main__':
    app.run(debug=True)