FROM ubuntu

WORKDIR /workspace

# ARGはビルド時に環境変数として使える。
# ENVで実行時の環境変数として設定できる。
ARG FOO1
ENV FOO2 $FOO1

# 同じ名前で実行時の環境変数を設定できる。
ARG BAR
ENV BAR $BAR

ADD test.sh ./

RUN echo $FOO1 > foo1.txt && \
    echo $FOO2 > foo2.txt && \
    echo $BAR > bar.txt

