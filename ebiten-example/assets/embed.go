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
)

func init() {
	img, _, err := ebitenutil.NewImageFromReader(bytes.NewReader(avatar))
	if err != nil {
		log.Fatal(err)
	}
	Img = img
}
