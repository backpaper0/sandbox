package main

import (
	"image/color"
	"log"

	"github.com/backpaper0/sandbox/ebiten-example/assets"
	"github.com/hajimehoshi/ebiten/v2"
	"github.com/hajimehoshi/ebiten/v2/vector"
)

type Game struct {
	ScreenWidth, ScreenHeight int
	playerX, playerY          int
	playerMotion              int //0：停止、1：歩き
	playerDir                 int //-1：左、1：右
	playerAnimation           int
}

func (g *Game) Update() error {
	g.playerMotion = 0
	if ebiten.IsKeyPressed(ebiten.KeyLeft) {
		g.playerMotion = 1
		g.playerDir = -1
		g.playerAnimation++
		g.playerX -= 4
	}
	if ebiten.IsKeyPressed(ebiten.KeyRight) {
		g.playerMotion = 1
		g.playerDir = 1
		g.playerAnimation++
		g.playerX += 4
	}
	if g.playerX < 0 {
		g.playerX = 0
	}
	if g.playerX > g.ScreenWidth-16 {
		g.playerX = g.ScreenWidth - 16
	}
	return nil
}

func (g *Game) Draw(screen *ebiten.Image) {
	vector.DrawFilledRect(screen, 0, 0, float32(g.ScreenWidth), float32(g.ScreenHeight), color.White, false)
	vector.DrawFilledRect(screen, 0, 120-16, float32(g.ScreenWidth), 16, color.Gray{0x90}, false)
	vector.DrawFilledRect(screen, 0, 120-16, float32(g.ScreenWidth), 1, color.Black, false)

	op := &ebiten.DrawImageOptions{}
	idx := 0
	if g.playerMotion == 1 {
		idx = g.playerAnimation / 2 % 2
	}
	if g.playerDir == -1 {
		op.GeoM.Scale(-1, 1)     //左右反転
		op.GeoM.Translate(16, 0) //反転してズレた分の補正
	}
	op.GeoM.Translate(float64(g.playerX), float64(g.playerY))
	screen.DrawImage(assets.RoboImgs[idx], op)
}

func (g *Game) Layout(outsideWidth, outsideHeight int) (screenWidth, screenHeight int) {
	return g.ScreenWidth, g.ScreenHeight
}

func main() {
	g := &Game{
		ScreenWidth: 320, ScreenHeight: 120,
		playerX: 32, playerY: 120 - (16 * 2),
		playerDir: 1,
	}
	ebiten.SetWindowSize(g.ScreenWidth*4, g.ScreenHeight*4)
	ebiten.SetWindowTitle("2Dプラットフォーマー")
	ebiten.SetTPS(30)
	if err := ebiten.RunGame(g); err != nil {
		log.Fatal(err)
	}
}
