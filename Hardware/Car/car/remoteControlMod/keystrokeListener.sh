# Griffin Saiia, gjs64
# keystroke listener --> takes 'wasd' and 'q', 'e', or 'p'
# 											and sends the proper state.

# terminated by ctrl-C
while true;
do
	read -rsn1 stroke
	if [ "$stroke" = "w" ]; then
		echo "1"
	fi
	if [ "$stroke" = "d" ]; then
		echo "5"
	fi
	if [ "$stroke" = "s" ]; then
		echo "3"
	fi
	if [ "$stroke" = "a" ]; then
		echo "6"
	fi
	if [ "$stroke" = "q" ]; then
		echo "9"
	fi
	if [ "$stroke" = "e" ]; then
		echo "13"
	fi
	if [ "$stroke" = "p" ]; then
		echo "12"
	fi
done
