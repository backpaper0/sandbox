package assets

import (
	"bytes"
	_ "embed"
	"log"

	"github.com/hajimehoshi/ebiten/v2"
	"github.com/hajimehoshi/ebiten/v2/ebitenutil"
)

const (
	SIZE = 128.0
)

var (
	//go:embed backpaper0.png
	avatar []byte
	Img    *ebiten.Image

	//go:embed robo1.png
	robo1 []byte
	//go:embed robo2.png
	robo2 []byte
	//go:embed robo3.png
	robo3    []byte
	RoboImgs []*ebiten.Image
)

func init() {
	Img = newImage(avatar)

	RoboImgs = []*ebiten.Image{
		newImage(robo1),
		newImage(robo2),
		newImage(robo3),
	}
}

func newImage(b []byte) *ebiten.Image {
	img, _, err := ebitenutil.NewImageFromReader(bytes.NewReader(b))
	if err != nil {
		log.Fatal(err)
	}
	return img
}
