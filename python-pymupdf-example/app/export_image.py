"""
poetry run python -m app.export_image -i data/demo.pdf -o data/images
"""
import argparse
from pathlib import Path
import fitz
import io
from PIL import Image

parser = argparse.ArgumentParser()
parser.add_argument("-i", "--input", type=Path)
parser.add_argument("-o", "--output", type=Path)
args = parser.parse_args()

input: Path = args.input
output: Path = args.output

if not output.exists():
    output.mkdir(parents=True)

with fitz.open(input) as doc:
    for page_index in range(doc.page_count):
        page = doc[page_index]
        images = page.get_images(full=True)
        for image_index, image in enumerate(images):
            xref = image[0]
            base_image = doc.extract_image(xref)
            image_bytes = base_image["image"]
            image_file = Image.open(io.BytesIO(image_bytes))
            image_file.save(output / f"page{page_index}_image{image_index}.png")
