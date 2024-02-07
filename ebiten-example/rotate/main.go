package main

import (
	"log"
	"math"

	"github.com/backpaper0/sandbox/ebiten-example/assets"
	"github.com/hajimehoshi/ebiten/v2"
)

type Game struct {
	ScreenWidth, ScreenHeight int
}

func NewGame() *Game {
	return &Game{
		ScreenWidth: assets.SIZE * 2, ScreenHeight: assets.SIZE * 2,
	}
}

func (g *Game) Update() error {
	return nil
}

func (g *Game) Draw(screen *ebiten.Image) {
	op := &ebiten.DrawImageOptions{}
	screen.DrawImage(assets.Img, op)

	op = &ebiten.DrawImageOptions{}
	op.GeoM.Rotate(90 * math.Pi / 180) //90度回転
	op.GeoM.Translate(assets.SIZE*2, 0)
	screen.DrawImage(assets.Img, op)

	op = &ebiten.DrawImageOptions{}
	op.GeoM.Rotate(180 * math.Pi / 180) //180度回転
	op.GeoM.Translate(assets.SIZE*2, assets.SIZE*2)
	screen.DrawImage(assets.Img, op)

	op = &ebiten.DrawImageOptions{}
	op.GeoM.Rotate(270 * math.Pi / 180) //270度回転
	op.GeoM.Translate(0, assets.SIZE*2)
	screen.DrawImage(assets.Img, op)
}

func reverseLR(img *ebiten.Image, op *ebiten.DrawImageOptions) {
	op.GeoM.Scale(-1, 1)              //左右反転
	op.GeoM.Translate(assets.SIZE, 0) //反転した分、x軸にプラス
}

func reverseUD(img *ebiten.Image, op *ebiten.DrawImageOptions) {
	op.GeoM.Scale(1, -1)              //左右反転
	op.GeoM.Translate(0, assets.SIZE) //反転した分、y軸にプラス
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
