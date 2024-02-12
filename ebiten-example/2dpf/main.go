package main

import (
	"image/color"
	"log"

	"github.com/backpaper0/sandbox/ebiten-example/assets"
	"github.com/hajimehoshi/ebiten/v2"
	"github.com/hajimehoshi/ebiten/v2/inpututil"
	"github.com/hajimehoshi/ebiten/v2/vector"
)

type Game struct {
	ScreenWidth, ScreenHeight int
	player                    *Player
}

type Player struct {
	x, y      int
	motion    int //0：停止、1：歩き
	dir       int //-1：左、1：右
	animation int
	jump      int //0：地面、1：上昇中、2：下降中
	vx, vy    int
}

func (g *Game) Update() error {
	p := g.player
	if ebiten.IsKeyPressed(ebiten.KeyLeft) {
		p.motion = 1
		p.dir = -1
		p.animation++
		if p.vx > -8 {
			p.vx -= 1
		}
	} else if ebiten.IsKeyPressed(ebiten.KeyRight) {
		p.motion = 1
		p.dir = 1
		p.animation++
		if p.vx < 8 {
			p.vx += 1
		}
	} else {
		p.motion = 0
		p.animation = 0
		p.vx = 0
	}
	p.x += p.vx
	if p.x < 0 {
		p.x = 0
		p.animation = 0
	}
	if p.x > g.ScreenWidth-16 {
		p.x = g.ScreenWidth - 16
		p.animation = 0
	}

	switch p.jump {
	case 0:
		if inpututil.IsKeyJustPressed(ebiten.KeySpace) {
			p.jump = 1
			p.vy = -16
		}
	case 1:
		p.vy += 2 //重力の影響
		if p.vy > 0 {
			p.jump = 2
		}
	case 2:
		p.vy += 1 //加速度
	}
	p.y += p.vy
	if p.y >= g.ScreenHeight-32 {
		p.y = g.ScreenHeight - 32
		p.vy = 0
		p.jump = 0
	}

	return nil
}

func (g *Game) Draw(screen *ebiten.Image) {
	vector.DrawFilledRect(screen, 0, 0, float32(g.ScreenWidth), float32(g.ScreenHeight), color.White, false)
	vector.DrawFilledRect(screen, 0, 120-16, float32(g.ScreenWidth), 16, color.Gray{0x90}, false)
	vector.DrawFilledRect(screen, 0, 120-16, float32(g.ScreenWidth), 1, color.Black, false)

	op := &ebiten.DrawImageOptions{}
	idx := 0
	p := g.player
	if p.motion == 1 {
		idx = p.animation / 2 % 2
	}
	if p.jump > 0 {
		idx = 2
	}
	if p.dir == -1 {
		op.GeoM.Scale(-1, 1)     //左右反転
		op.GeoM.Translate(16, 0) //反転してズレた分の補正
	}
	op.GeoM.Translate(float64(p.x), float64(p.y))
	screen.DrawImage(assets.RoboImgs[idx], op)
}

func (g *Game) Layout(outsideWidth, outsideHeight int) (screenWidth, screenHeight int) {
	return g.ScreenWidth, g.ScreenHeight
}

func main() {
	p := &Player{
		x: 32, y: 120 - (16 * 2), dir: 1,
	}
	g := &Game{
		ScreenWidth: 320, ScreenHeight: 120,
		player: p,
	}
	ebiten.SetWindowSize(g.ScreenWidth*4, g.ScreenHeight*4)
	ebiten.SetWindowTitle("2Dプラットフォーマー")
	ebiten.SetTPS(30)
	if err := ebiten.RunGame(g); err != nil {
		log.Fatal(err)
	}
}
