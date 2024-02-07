package main

import (
	"bytes"
	"log"

	"github.com/backpaper0/sandbox/ebiten-example/assets"
	"github.com/hajimehoshi/ebiten/v2"
	"github.com/hajimehoshi/ebiten/v2/ebitenutil"
)

type Game struct {
	ScreenWidth, ScreenHeight int
	Img                       *ebiten.Image
}

func NewGame() *Game {
	img, _, err := ebitenutil.NewImageFromReader(bytes.NewReader(assets.Avatar))
	if err != nil {
		log.Fatal(err)
	}
	return &Game{
		ScreenWidth: 256, ScreenHeight: 256,
		Img: img,
	}
}

func (g *Game) Update() error {
	return nil
}

func (g *Game) Draw(screen *ebiten.Image) {
	op := &ebiten.DrawImageOptions{}
	screen.DrawImage(g.Img, op)

	//左右反転
	op = &ebiten.DrawImageOptions{}
	reverseLR(g.Img, op)
	op.GeoM.Translate(float64(g.Img.Bounds().Dx()), 0)
	screen.DrawImage(g.Img, op)

	//上下反転
	op = &ebiten.DrawImageOptions{}
	reverseUD(g.Img, op)
	op.GeoM.Translate(0, float64(g.Img.Bounds().Dy()))
	screen.DrawImage(g.Img, op)

	//上下左右反転
	op = &ebiten.DrawImageOptions{}
	reverseUD(g.Img, op)
	reverseLR(g.Img, op)
	op.GeoM.Translate(float64(g.Img.Bounds().Dx()), 0)
	op.GeoM.Translate(0, float64(g.Img.Bounds().Dy()))
	screen.DrawImage(g.Img, op)
}

func reverseLR(img *ebiten.Image, op *ebiten.DrawImageOptions) {
	op.GeoM.Scale(-1, 1)                             //左右反転
	op.GeoM.Translate(float64(img.Bounds().Dx()), 0) //反転した分、x軸にプラス
}

func reverseUD(img *ebiten.Image, op *ebiten.DrawImageOptions) {
	op.GeoM.Scale(1, -1)                             //左右反転
	op.GeoM.Translate(0, float64(img.Bounds().Dy())) //反転した分、y軸にプラス
}

func (g *Game) Layout(outsideWidth, outsideHeight int) (screenWidth, screenHeight int) {
	return g.ScreenWidth, g.ScreenHeight
}

func main() {
	g := NewGame()
	ebiten.SetWindowSize(g.ScreenWidth, g.ScreenHeight)
	ebiten.SetWindowTitle("上下左右反転")
	if err := ebiten.RunGame(g); err != nil {
		log.Fatal(err)
	}
}
