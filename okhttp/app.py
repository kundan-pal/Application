from flask import Flask, jsonify, request
import base64
from PIL import Image
import os

# os.system("dir yolov5-master")
# os.system("cd yolov5-master && dir")
# exp = "exp"
# path = "yolov5-master/runs/detect/" + exp + "/labels/image.txt"
# path = "type " + path
# print(path)

labels = {0: 'person', 1: 'bicycle', 2: 'car', 3: 'motorcycle', 4: 'airplane', 5: 'bus', 6: 'train', 7: 'truck', 8: 'boat', 9: 'traffic', 10: 'fire', 11: 'stop', 12: 'parking', 13: 'bench', 14: 'bird', 15: 'cat', 16: 'dog', 17: 'horse', 18: 'sheep', 19: 'cow', 20: 'elephant', 21: 'bear', 22: 'zebra', 23: 'giraffe', 24: 'backpack', 25: 'umbrella', 26: 'handbag', 27: 'tie', 28: 'suitcase', 29: 'frisbee', 30: 'skis', 31: 'snowboard', 32: 'sports', 33: 'kite', 34: 'baseball', 35: 'baseball', 36: 'skateboard', 37: 'surfboard', 38: 'tennis', 39: 'bottle', 40: 'wine', 41: 'cup', 42: 'fork', 43: 'knife', 44: 'spoon', 45: 'bowl', 46: 'banana', 47: 'apple', 48: 'sandwich', 49: 'orange', 50: 'broccoli', 51: 'carrot', 52: 'hot', 53: 'pizza', 54: 'donut', 55: 'cake', 56: 'chair', 57: 'couch', 58: 'potted', 59: 'bed', 60: 'dining', 61: 'toilet', 62: 'tv', 63: 'laptop', 64: 'mouse', 65: 'remote', 66: 'keyboard', 67: 'cell', 68: 'microwave', 69: 'oven', 70: 'toaster', 71: 'sink', 72: 'refrigerator', 73: 'book', 74: 'clock', 75: 'vase', 76: 'scissors', 77: 'teddy', 78: 'hair', 79: 'toothbrush'}
# labels = {}
# temp = open("temp.txt",'r')
# temp_lines = temp.readlines()
# for x in temp_lines:
#     row = x.split()
#     id = int(row[0].split(':')[0])
#     object_name = row[1]
#     labels[id] = object_name
#     # print(id, object_name)
# # print(labels)
# print(qwe)
# print(qwe[53])
# exp = ""
# for path in os.listdir("yolov5-master/runs/detect"):
#     exp = path
    # print(path)

# path = "yolov5-master/runs/detect/" + exp + "/labels/image.txt"

# file = open(path, 'r') 
# lines = file.readlines()
# # print(lines)
# class_id = []
# for x in lines:
#     class_id.append(int(x.split(" ")[0]))
#     # print(x.split(" ")[0])
# print(class_id)

# os.system("dir")
# os.system(path)
# os.system("type {}".format(path))
app = Flask(__name__)

@app.route('/', methods=['GET', 'POST'])
def handle_request():
    if request.method == 'GET':
        return jsonify("Flask Server & Android are Working Successfully !!!")
    if request.method == 'POST':
        data =  request.data
        print(len(data))
        image = base64.b64decode(data)
        filename = "yolov5-master/image.jpg"
        with open(filename, 'wb') as f:
            f.write(image)
        
        image = Image.open('yolov5-master/image.jpg')

        os.system("cd yolov5-master && python detect.py --weights yolov5s.pt --img 640 --conf 0.25 --source image.jpg --save-txt --save-conf")


        # print(len(os.listdir("yolov5-master/runs/detect")))
        
        dir_list = os.listdir("yolov5-master/runs/detect")
        l2 = sorted(dir_list, key=len)
        print(l2[-1])
        exp = l2[-1]
        # dir_list.sort()
        # print(dir_list[-1])

        # exp = ""
        # for x in os.listdir("yolov5-master/runs/detect"):
        #     print(x)
        #     exp = x
            # print(path)
        # print(exp)
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
        # for path in os.listdir("yolov5-master\runs\detect"):
        #     print(path)

        # os.path.isfile(os.path.join(dir_path, "yolov5-master\runs\detect"))
        # file = open("")
        print(len(data))
        # print(result) 
        image.show()
        return jsonify(result)


# @app.route('/returnjson', methods = ['GET'])
# def ReturnJSON():
#     if(request.method == 'GET'):
#         data = {
#             "Modules" : 15,
#             "Subject" : "Data Structures and Algorithms",
#         }
  
#         return jsonify(data)
  
if __name__=='__main__':
    # app.run(host="0.0.0.0", port=5000, debug=True)
    app.run(debug=True)

# @app.route('/')
# def index():
#     return jsonify(
#         userId = 1,
#         id=1,
#         title = "Returning object from server!!!",
#         completed = False
#     )
#     return jsonify("Return object from server!!!")