# java -cp .:samplePlayer.jar BattleshipMain -v ../config.txt ../loc1.txt ../loc2.txt sample sample
# random | greedy | prob | custom
rm *.log
java -cp .:samplePlayer.jar BattleshipMain -l "$1_v_$2".log ../config.txt ../loc1.txt ../loc2.txt "$1" "$2"
# cat "$1".txt

