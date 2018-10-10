#############################################################
# Created by: Krishnan, Keerthi
# kvkrishn
# 14 August 2018
#
# Assignment: Lab 5: ASCII Binary to Decimal 
# CMPE 012, Computer Systems and Assembly Language
# UC Santa Cruz, Summer 2018
#
# Description: This program takes a binary value input by the user and converts it to the decimal equivalent. 
#
# Pseudocode:
# 
# binary value entered: n bits 
# For i from 1 to n:
#	Sign label:
# 		If first bit = 1, then go to negative label 
#		If 0, continue the same arithmetic down 
#	Hexadecimal: 
# 		take bits by group of 4 and if not enough bits are present(there are not 32 bits), add 0's to the right to make it 32 bits
#		Split groups of 4 and print the hexadecimal value of each of these pairs of 4
#		Print the value of hexadecimal
#	Negative Label:
#		perform 2's Complement arithmetic(flip all bits in binary number and add 1)
# 		to get decimal value, create running sum variable
#		multiply each bit by a constant 2^n where n decreases by one each time we move to the next bit, and n starts at n-1
#		add all of those values together and find the ascii value as well as the ascii value of negative and print
#		exit program ..
#	Positive Label:
#		to get decimal value, create running sum variable
#		multiply each bit by a constant 2^n where n decreases by one each time we move to the next bit, and n starts at n-1
#		add all of those values together and find the ascii value as well in order to print
#		exit program
#################################################################

.text 
main: 
	move $s0, $a1 			     		# take the binary number from a1 and store in s0
	lw $s0, ($s0)			     		# load the word from $s0
	
	li $v0, 4			     		# syscall and print 
	la $a0, binaryPrompt                		# print out binary prompt 
	syscall

	li $v0, 4			     		# read input string and print to console      
	move $a0, $s0			     		# move $s0 to $a0 to print
	syscall 
	
	la $a0, newLine			     		# print new line 
	syscall
	
	move $t0, $s0			     		# move $s0 to $t0
	lb $t2, 0($t0)			     		# load the first byte from $t0 into $t2
	
	li $s1, 7  			     		#s1 is loop counter
	li $s0, 0
	bne $t2, 49, convert_loop  	     		# if the ascii value of the first bit is not equal to 49, which is not 1, the binary number is positive and should be converted
	addiu $s0, $s0 0xFFFFFF00  	     		# add 0xFFFFFF00 in order to sign extend 32 bits if negative 
	
	convert_loop:
		lb $t3, 0($t0) 		    	        # t3 is current byte (either 0 or 1)
		beqz $t3, exit		     		# if null character is reached, exit loop
		subi $t3, $t3, 48            		# subtract 48 to get 0 or 1 
		sllv $t3, $t3, $s1	    	 	# shift logical left to sign extend positive
		addu $s0, $s0, $t3	     		# add to s0
		addiu $t0, $t0, 1	     		# increment address by 1
		subiu $s1, $s1, 1	     		# subtract address by 1
		b convert_loop   	     		# branch back until statement above is clear
        	
        	exit:		    
        	li $t0, 0xF0000000	     		# t0 is our mask of 4 bits
        	li $t1, 0	             		# t1 is our temp register to hold the mask result
        	li $t2, 28		     		# t2 is loop counter
        	
        	li $v0, 4	             		# syscall and print 
		la $a0, hexPrompt            		# print out hex prompt 
		syscall			     
        	
        	li $v0, 11		    		# syscall to print character
       		hex_loop:	
             		beqz $t0, end	     		# until the hexadecimal reaches the null character, continue
             		and $t1, $s0, $t0    		# and to get certain bits 
             		srlv $t1, $t1, $t2   		# shift logical right and store in t1
             		subiu $t2, $t2, 4    		# subtract by 4 to get 4 bits 
        		srl $t0, $t0, 4	     		# shift logical right by 4 everytime 
             		bgeu $t1, 10, greaterTen	# if the value of t1 is greater than ten, then it is a letter representation
             		addiu $t1, $t1, 48		# otherwise, add t1 by 48
             		move $a0, $t1			# move to $a0 and print character
             		syscall
             		b hex_loop			# branch back to hex loop
             		
             		greaterTen:			# greater than ten function
             			addiu $t1, $t1, 55	# add t1 by 55
             			move $a0, $t1		# move into a0 and print character
             			syscall
                		b hex_loop		# branch back to hex loop
                		
                end: 
                	li $v0, 4			# syscall and print 
			la $a0, decPrompt               # print out decimal prompt 
			syscall
			
			li $t4, 0			# counter for loop
			li $t2, 10 			# radix of decimal
			andi $t0, $s0, 255		# and with 255 
			bnez $t0, nonZero		# if not equal to zero, then branch to nonZero
			li $v0, 11			# else call syscall 11
			li $a0, 48			# load 48(0) in a0
			syscall
			b finish			# exit program
			
			nonZero:
			andi $t1, $t0, 128		# and t0 with 128 to get 
			li $v0, 11			# print character syscall loaded into v0
			bne $t1, 128, dec_loop		# as long as t1 does not equal 128, go to dec_loop
			li $a0, 45 			# If not, 45 is the ASCII for '-', therefore load into v0 and syscall
			syscall
			xori $t0, $t0, 255		# xor with 255 to flip all bits 
			addiu $t0, $t0, 1		# add by 1 to get twos comp
			
			dec_loop:
				beqz $t0, print_loop	# if t0 equals 0, then go to print_loop
				div $t0, $t2		# divide the decimal number by the base
				mfhi $t3 		# store remainder in t3 register
				mflo $t0		# store quotient in t0 register
				addiu $t3, $t3, 48	# add t3 by 48
				sb $t3, ($sp)		# store in stack pointer
				subi $sp, $sp, 1	# subtract by 1 to go up the stack
				addiu $t4, $t4, 1	# update counter
				b dec_loop		# go back to dec_loop
				
			print_loop:
				beqz $t4, finish	# when t4 equals 0, exit program
				addi $sp, $sp, 1 	# return to original address
				lb $a0, ($sp)		# load value into $a0 from $sp
				syscall
				subiu $t4, $t4, 1	# go back down the stack
				b print_loop		# branch back to print_loop
				
			finish:
				li $v0, 10		# exit program with syscall 10
				syscall
	
.data 

binaryPrompt: .asciiz "\nYou entered the binary number: \n"
hexPrompt: .asciiz "The hex representation of the sign-extended number is: \n0x"
decPrompt: .asciiz "\nThe number in decimal is: \n"
newLine: .asciiz  "\n"
