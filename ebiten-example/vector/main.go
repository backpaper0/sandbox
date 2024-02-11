package main

import (
	"image/color"
	"log"

	"github.com/hajimehoshi/ebiten/v2"
	"github.com/hajimehoshi/ebiten/v2/vector"
)

type Game struct {
	ScreenWidth, ScreenHeight int
}

func NewGame() *Game {
	return &Game{
		ScreenWidth: 1200, ScreenHeight: 800,
	}
}

func (g *Game) Update() error {
	return nil
}

func (g *Game) Draw(screen *ebiten.Image) {
	dx := screen.Bounds().Dx()
	dy := screen.Bounds().Dy()
	vector.DrawFilledRect(screen, 0, 0, float32(dx), float32(dy), color.White, false)

	strokeRect := func(y float32, sw float32) {
		c := color.RGBA{0x99, 0xFF, 0x99, 0xFF}
		s := float32(50)
		//長方形で指定する座標は左上
		vector.StrokeRect(screen, 100, y, s, s, sw, c, false)
		//内側：座標へ線の幅の半分を加算、サイズから線の幅を減算
		vector.StrokeRect(screen, 200+(sw/2), y+(sw/2), s-sw, s-sw, sw, c, false)
		//外側：座標から線の幅の半分を減算、サイズへ線の幅を加算
		vector.StrokeRect(screen, 300-(sw/2), y-(sw/2), s+sw, s+sw, sw, c, false)
	}
	strokeRect(100, 4)
	strokeRect(200, 8)
	strokeRect(300, 16)

	strokeCircle := func(y float32, sw float32) {
		c := color.RGBA{0x99, 0x99, 0xFF, 0xFF}
		r := float32(25)
		//円で指定する座標は中心
		vector.StrokeCircle(screen, 475, y, r, sw, c, true)
		//内側：半径から線の幅の半分を減算
		vector.StrokeCircle(screen, 575, y, r-(sw/2), sw, c, true)
		//外側：半径へ線の幅の半分を加算
		vector.StrokeCircle(screen, 675, y, r+(sw/2), sw, c, true)
	}
	strokeCircle(125, 4)
	strokeCircle(225, 8)
	strokeCircle(325, 16)

	fillRect := func(y float32) {
		c := color.RGBA{0x99, 0xFF, 0x99, 0xFF}
		s := float32(50)
		//長方形で指定する座標は左上
		vector.DrawFilledRect(screen, 100, y, s, s, c, false)
		vector.DrawFilledRect(screen, 200, y, s, s, c, false)
		vector.DrawFilledRect(screen, 300, y, s, s, c, false)
	}
	fillRect(400)

	fillCircle := func(y float32) {
		c := color.RGBA{0x99, 0x99, 0xFF, 0xFF}
		r := float32(25)
		//円で指定する座標は中心
		vector.DrawFilledCircle(screen, 475, y, r, c, true)
		vector.DrawFilledCircle(screen, 575, y, r, c, true)
		vector.DrawFilledCircle(screen, 675, y, r, c, true)
	}
	fillCircle(425)

	{ //格子
		sw := float32(1)
		c := color.Gray{0xC0}
		s := 50
		for x := 0; x < dx; x += s {
			vector.StrokeLine(screen, float32(x), 0, float32(x), float32(dy), sw, c, false)
		}
		for y := 0; y < dy; y += s {
			vector.StrokeLine(screen, 0, float32(y), float32(dx), float32(y), sw, c, false)
		}
	}
}

func (g *Game) Layout(outsideWidth, outsideHeight int) (screenWidth, screenHeight int) {
	return g.ScreenWidth, g.ScreenHeight
}

func main() {
	g := NewGame()
	ebiten.SetWindowSize(g.ScreenWidth, g.ScreenHeight)
	ebiten.SetWindowTitle("vector package")
	if err := ebiten.RunGame(g); err != nil {
		log.Fatal(err)
	}
}
