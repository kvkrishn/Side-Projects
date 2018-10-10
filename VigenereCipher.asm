# Program that implements encryption and decryption of vigenere cipher
# Keerthi Krishnan 
# kvkrishn@ucsc.edu
# March 11, 2018
# Lab 6
# Section 01J, Ehsan Hemmati 


#################################################################################################
# Pseudocode:

# EncryptChar:

# if it is an uppercase value, then do this:
  #  subtract 65 from uppercase value in plaintext string to get value in alphabet from 0 - 25
   # subtract 65 from uppercase value in key string to get value in alphabet from 0 - 25
   # add those two values together, say it is y
   # if y is greater than 25, then subtract 26 to get alphabet letter value from 0 - 25
   # add 65 to y in order to get an ascii value for the character
   # store y into $v0 and print it out

# if it is lowercase:
    # subtract 97 from uppercase value in plaintext string to get value in alphabet from 0 - 25
    # subtract 97 from uppercase value in key string to get value in alphabet from 0 - 25
    # add those two values together, say it is y
    # if y is greater than 25, then subtract 26 to get alphabet letter value from 0 - 25
    # add 97 to y in order to get an ascii value for the character
    # store y into $v0 and print it out

# if plaintext contains special characters:
    # store original ascii value from plaintext register into v0
    # print it out without doing any encrypting 
    # do not increment address of key string
    # increment address of plaintext to move to next byte

# if plaintext's character is greater than key's character:
    # move the original address of key string into a temporary register
    # branch back to the main loop to do conversions

# if more than 30 characters in plaintext:
    # create counter for every time I encrypt a character 
    # when counter is greater than or equal to 30, then exit the program

# EncryptString:

    # get key character from register 
    # get plaintext character from register 
    # check if it is encryptable, if it is, then go to the normal loop
    # if not encryptable, then store and print character
    # decrement key address
            # normal loop:
               # get character of key 
               # get character of plaintext 
               # call EncryptChar 
               # store encrypted character in cipher string register 
               # if plaintext hits null character, exit the program
               # increment all 3 sequences

# DecryptChar:

# if it is an uppercase value, then do this:
    # subtract 65 from uppercase value in plaintext string to get value in alphabet from 0 - 25
    # subtract 65 from uppercase value in key string to get value in alphabet from 0 - 25
    # subtract those two values together, say it is y
    # if y is less than 0, then add 26 to get alphabet letter value from 0 - 25
    # add 65 to y in order to get an ascii value for the character
    # store y into $v0 and print it out

# if it is lowercase:
    # subtract 97 from uppercase value in plaintext string to get value in alphabet from 0 - 25
    # subtract 65 from uppercase value in key string to get value in alphabet from 0 - 25
    # subtract those two values together, say it is y
    # if y is less than 0, then add 26 to get alphabet letter value from 0 - 25
    # add 97 to y in order to get an ascii value for the character
    # store y into $v0 and print it out

# if plaintext contains special characters:
   # store original ascii value from plaintext register into v0
    # print it out without doing any encrypting 
    # do not increment address of key string
    # increment address of plaintext to move to next byte

# if plaintext's character is greater than key's character:
   # move the original address of key string into a temporary register
   # branch back to the main loop to do conversions

# if more than 30 characters in plaintext:
    # create counter for every time I encrypt a character 
    # when counter is greater than or equal to 30, then exit the program

# DecryptString:

#    get key character from register 
#    get plaintext character from register 
#    check if it is encryptable, if it is, then go to the normal loop
#    if not encryptable, then store and print character
#    decrement key address
#            normal loop:
#                get character of key 
#                get character of plaintext 
#                call DecryptChar 
#                store encrypted character in cipher string register 
#                if plaintext hits null character, exit the program
#                increment all 3 sequences
#######################################################################################################



.text 
# methods for encryption

EncryptString:  


move $s0, $a0 #save the address of $a0 in $s0
move $s1, $a1 # s1 is the saved key address

move $t0, $s0 # move saved address of $a0 into $t0
move $t1, $s1 # move saved address of $a1 into $t1
move $t6, $s1 # $t6 is the pointer to increment when key hits null character 
li $t7, 0 # counter to keep track of number of characters encrypted

EncryptLoop:
	
	lb $t0, ($s0) # load the byte from plaintext 
	
	bge $t0, 65, normal_loop # if its an uppercase letter, then go to normal loop
	bge $t0, 97, normal_loop # if its lowercase, then also go to normal loop
	move $a0, $t0 # if its a special character, move the special character in $a0
	move $v0, $a0 # move special character into $v0
	addi $t7, $t7, 1 # increment character counter
	sb $v0, ($a2) # store byte
	
	addi $s0, $s0, 1 # increment address of plaintext 
	addi $a2, $a2, 1 # increment cipher address 
	beqz $t0, exit # when plaintext hits null character, exit
	
	# this loop is for normal characters that are not special characters
	normal_loop:
		
		lb $t0, ($s0) # load the character from plaintext string
		lb $t1, ($s1) # load the character from key string
		beqz $t0, exit # if plaintext hits null character, exit 
		beqz $t1, __reset # if key hits null character, then reset key 
		sw $ra, ($sp) # store word from stack pointer 
		subi $sp, $sp, 4 # subtract to move up stack
		
		move $a0, $t0 # move character from $t0 to $a0, so it does not output address
		move $a1, $t1 # same as above 
		
		jal EncryptChar # call EncryptChar to encrypt our characters
		
		sb  $v0, ($a2) # store the output character from $a2 into $v0
		
		addi $sp, $sp, 4 # move down the stack
		lw $ra, ($sp) # load the word from stack pointer 
		
		bge $t7, 30, exit # if the counter for the characters encrypted hits 30, then exit
		addi $s0, $s0, 1 # increment address of plaintext
		addi $s1, $s1, 1 # increment address of key 
		addi  $a2, $a2, 1 # increment address of output

		b   EncryptLoop # branch back to EncryptLoop

# exit command	
exit:
	jr $ra


	
.text 
# method to encrypt characters 
EncryptChar:  

	blt $a0, 65, EncryptLoop # if special character, branch back to EncryptLoop
	bge $a0, 97, __lowercase_convert # if its a lowercase character, go to lowercase conversion
	bge $a0, 65, __uppercase_convert # if uppercase character, go to uppercase conversion


__uppercase_convert:
	bgt $a0, 90, EncryptLoop # if special character,branch back to EncryptLoop
	sub   $t2, $a0, 65 # subtract to get alphabet numerical value 
	sub  $t3, $a1, 65 # same as above 

	add    $t4, $t2, $t3 # add the two numerical values together 

	bgt $t4, 25, __subtract_26 # if the output of addition is greater than 25, then subtract 26 and then add 65
	addiu $t4, $t4, 65 # add 65 to get corresponding ascii value

	move $v0, $t4 # move to v0 to store address
	addi $t7, $t7, 1
	jr $ra 

	
__subtract_26:

	sub $t5, $t4, 26 # subtract 26
	addiu $t5, $t5, 65 # add 65 to get corresponding ascii value
	move $v0, $t5 # move to v0 to store address
	addi $t7, $t7, 1 # increment the counter
	jr $ra
	
__lowercase_convert:
	bgt  $a0, 122, EncryptLoop # if special character, branch to EncryptLoop
	sub $t2, $a0, 97 # subtract to get alphabet numerical value 
	sub $t3, $a1, 65 # subtract key from 65, as key is always capitalized with no special characters
	add $t4, $t2, $t3 # add the two numerical values together 

	bgt $t4, 25, __subtract_26_LC # if output is greater than 25, then subtract 26 and add 97 to get value 
	addiu $t4, $t4, 97 # add 97 to get value
	move $v0, $t4 # move value into $v0
	addi $t7, $t7, 1 # increment counter 
	jr $ra
	
__subtract_26_LC:
	sub $t5, $t4, 26 # subtract 26
	addiu $t5, $t5, 97 # add that value by 97 to get ascii value
	move $v0, $t5 # move to $v0
	addi $t7, $t7, 1 # increment by 1
	jr $ra


__reset:

	move $s1, $t6 # store the original address of the key in $s1 again 
	b   normal_loop # brach back to normal loop
	jr $ra
	
# methods for decryption 
	
DecryptString:

move $s0, $a0 #save the address of $a0 in $s0: saved plaintext address
move $s1, $a1 # s1 is the saved key address

move $t0, $s0 # move saved plaintext address into $s0
move $t1, $s1 # t1 is the pointer to increment when key hits null character 
move $t6, $s1 # move saved key address into a temporary register to increment later 
li $t7, 0 # counter for number of characters

DecryptLoop:
	lb $t0, ($s0) # load character into $t0 from plaintext
	
	bge $t0, 65, normal1_loop # if uppercase alphabetic character, go to normal loop
	bge $t0, 97, normal1_loop # if lowercase alphabetic character, go to normal loop
	move $a0, $t0 # move the value in $t0 into $a0
	move $v0, $a0 # move value into $a0
	addi $t7, $t7, 1 # increment counter by 1 
	sb $v0, ($a2) # store byte from output into v0
	
	addi $s0, $s0, 1 # increment address of plaintext by 1
	addi $a2, $a2, 1 # increment address of output by 1
	beqz $t0, exit1 # if plaintext hits the null character, then exit1
	
	# loop for non-special characters, aka uppercase and lowercase alphabetic
	normal1_loop:
		lb $t0, ($s0) # load the character of plaintext
		lb $t1, ($s1) # load the character of key 
		
		beqz $t0, exit1 # if plaintext hits null character, then exit 
		beqz $t1, __reset1 # if key hits null character, then reset the key back to the beginning
		sw $ra, ($sp) # store word of stackpointer
		subi $sp, $sp, 4 # subtract to move up the stack
		
		move $a0, $t0 # move value of $t0 into $a0
		move $a1, $t1 # move value of $t1 into $a1
		
		jal DecryptChar # call DecryptChar function
		
		sb  $v0, ($a2) # store the byte of the output into $v0
		
		addi $sp, $sp, 4 # add to move down the stack
		lw $ra, ($sp)  # load the word from stack pointer
		
		
		bge $t7, 30, exit1 # if the counter for characters reaches 30 or is greater, then exit 
		addi $s0, $s0, 1 # increment plaintext address
		addi $s1, $s1, 1 # increment key address
		addi  $a2, $a2, 1 # increment output address
		 
		b   DecryptLoop # branch back to DecryptLoop until above statements satisfy

# exit label	
exit1:
	jr $ra

# method to decrypt characters
DecryptChar:

	blt $a0, 65, DecryptLoop # if special character, branch to DecryptLoop
	bge $a0, 97, __lowercase_convert1 # if lowercase alphabetic, then go to lowercase label
	bge $a0, 65, __uppercase_convert1 # if uppercase alphabetic, then go to uppercase label


__uppercase_convert1:
	bgt $a0, 90, DecryptLoop # if special character, branch to DecryptLoop
	sub $t2, $a0, 65 # subtract to get alphabet numerical value 
	sub $t3, $a1, 65 # same as above 

	sub  $t4, $t2, $t3 # subtract the two numerical values together 
	
	
	bltz  $t4,  __add_26 # if the value is less than zero, then add 26 and add 65 to get value
	addiu $t4, $t4, 65 # add 65 to get corresponding ascii value

	move $v0, $t4 # move to v0 to store address
	addi $t7, $t7, 1 # increment counter
	jr $ra 

	
__add_26:

	add $t5, $t4, 26 # add 26
	addiu $t5, $t5, 65 # add 65 to get corresponding ascii value
	move $v0, $t5 # move to v0 to store address
	addi $t7, $t7, 1 # increment counter
	jr $ra
	
__lowercase_convert1:
	bgt  $a0, 122, DecryptLoop # if special character, go to DecryptLoop
	sub $t2, $a0, 97 # subtract to get alphabet numerical value 
	sub $t3, $a1, 65 # subtract 65 for key value
	sub  $t4, $t2, $t3 # add the two numerical values together 

	bltz  $t4, __add_26_LC # if value is less than zero, then add 26, and then add 97 for normal value
	addiu $t4, $t4, 97 # add 97 
	move $v0, $t4 # move to $v0
	addi $t7, $t7, 1 # increment counter
	jr $ra
	
__add_26_LC:
	add  $t5, $t4, 26 # add 26
	addiu $t5, $t5, 97 # add 97 to get ascii value
	move $v0, $t5 # move into $v0
	addi $t7, $t7, 1 # increment counter
	jr $ra

__reset1:

	move $s1, $t6 # store original address of key back into saved register
	b   normal1_loop # branch back to the normal loop
	jr $ra
	