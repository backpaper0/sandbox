import argparse

import weave
from dotenv import load_dotenv

load_dotenv()

weave.init("object_example")


parser = argparse.ArgumentParser()
parser.add_argument("-s", help="保存", action="store_true", default=False)
parser.add_argument("-g", help="取得", action="store_true", default=False)
args = parser.parse_args()

if args.s:
    # オブジェクトの保存
    weave.publish(["foo", "bar", "baz"], "example_obj")

if args.g:
    # オブジェクトの取得
    obj = weave.ref("example_obj").get()
    print(obj)
