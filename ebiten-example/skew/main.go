package main

import (
	"fmt"
	"image/color"
	"log"

	"github.com/backpaper0/sandbox/ebiten-example/assets"
	"github.com/hajimehoshi/ebiten/v2"
	"github.com/hajimehoshi/ebiten/v2/inpututil"
	"github.com/hajimehoshi/ebiten/v2/text"
	"golang.org/x/image/font/basicfont"
)

type Game struct {
	ScreenWidth, ScreenHeight int
	X, Y                      float64
}

func NewGame() *Game {
	return &Game{
		ScreenWidth: assets.SIZE * 2, ScreenHeight: assets.SIZE * 2,
	}
}

func (g *Game) Update() error {
	if inpututil.IsKeyJustPressed(ebiten.KeyUp) {
		g.Y += 0.1
	}
	if inpututil.IsKeyJustPressed(ebiten.KeyDown) {
		g.Y -= 0.1
	}
	if inpututil.IsKeyJustPressed(ebiten.KeyLeft) {
		g.X -= 0.1
	}
	if inpututil.IsKeyJustPressed(ebiten.KeyRight) {
		g.X += 0.1
	}
	return nil
}

func (g *Game) Draw(screen *ebiten.Image) {
	op := &ebiten.DrawImageOptions{}
	op.GeoM.Skew(g.X, g.Y)
	screen.DrawImage(assets.Img, op)

	text.Draw(screen, fmt.Sprintf("%g, %g", g.X, g.Y), basicfont.Face7x13, 8, 200, color.White)
}

func (g *Game) Layout(outsideWidth, outsideHeight int) (screenWidth, screenHeight int) {
	return g.ScreenWidth, g.ScreenHeight
}

func main() {
	g := NewGame()
	ebiten.SetWindowSize(g.ScreenWidth, g.ScreenHeight)
	ebiten.SetWindowTitle("skew")
	if err := ebiten.RunGame(g); err != nil {
		log.Fatal(err)
	}
}
