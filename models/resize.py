from PIL import Image
import os, sys

tiles_width = 160
tiles_height = 160

meeple_width = 40
meeple_height = 40

def resize_tiles(path, width, height):
    dirs = os.listdir( path )

    for item in dirs:
        if os.path.isfile(path+item):
            im = Image.open(path+item)
            f, e = os.path.splitext(path+item)
            imResize = im.resize((width,height), Image.ANTIALIAS)
            imResize.save(f + '.jpg', 'JPEG', quality=100)

def resize_meeples(path, width, height):
    dirs = os.listdir( path )

    for item in dirs:
        if os.path.isfile(path+item):
            im = Image.open(path+item)
            f, e = os.path.splitext(path+item)
            imResize = im.resize((width,height), Image.ANTIALIAS)
            imResize.save(f + '.png', 'png')

# resize_tiles("./tiles/", tiles_width, tiles_height)
resize_meeples("./meeples/", meeple_width, meeple_height)