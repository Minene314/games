# 作成したゲームを置いておくリポジトリ
・オセロ  
・中置記法を認識する計算機  
・ガイスター

## オセロ
### コンパイル方法
	$javac -encoding utf-8 Othello/*.java
### 実行方法
	$java Othello.Main
### 遊び方
実行後、プレイヤーネームを入力する。  
ここで"randcom","negacom","abcom"もしくは"moncom"と入力するとvs COMができる。  
あとはオセロの流れに沿ってゲームするだけ。  

## アプレットの中置記法を認識する計算機
### コンパイル方法
	$javac -encoding utf-8 calculator/*.java
### 実行方法
	$appletviewer calcAction.html
### 遊び方
ただの弱い計算機ですゆえ...   
入力例  
((1*2)+3)+(4-3)  
((1+2)+(((3+(4+(5+6)))+(7+8))+9))+10  

かっこで演算子の優先順位をつけているので同じ高さに演算子が２つ以上あると計算結果が正しく表示されません

## ガイスター
### コンパイル方法
	$javac -encoding utf-8 Geister/*.java
### 実行方法
	$java Geister.Main
### 遊び方
詳しいルールは調べてください()  
実行後、自分の青駒の位置を指定してゲームスタート  
先手後手はランダムに決まります  
相手はランダムに手を指すコンピュータです  
これから色々と工夫していくつもりです  


とりあえずはコードの構造の見直しと書き直しをしていく予定です  
