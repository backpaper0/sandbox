# 変数を定義できる。$(VAR_NAME)で参照できる。
TXTFILE=hello.txt

# ターゲットがファイル名でない場合に事故を防ぐため.PHONYを書く。
.PHONY: all
all: $(TXTFILE)

.PHONY: clean
clean:
	rm $(TXTFILE)

# ターゲットはワイルドカード(%)を使える。
# コマンド内では$@でターゲット名を参照できる。
%.txt:
	touch $@
