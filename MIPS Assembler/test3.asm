	.text
main:	sw $t5,12($s5)
	sw $s2,0($t0)
	sw $s4,-73($t2)
	sw $t0,1($s3)
	sw $s7,-1($t1)
label1:	lw $t2,-120($t1)
	lw $s5,0($t2)
	lw $t2,213($s1)
	lw $s2,1($t4)
	lw $s2,-1($s3)
	bne $t2,$t3,label1
	bne $t2,$t3,label2
	bne $t2,$t2,label1
	bne $t2,$t2,label2
	bne $s4,$0,label1
	bne $0,$s1,label2
label2:	add $t1,$t2,$t3
	j label1
	j label2
	j l3
	j l4
	la $t1,s1
	la $t2,s2
	la $s1,w1
	la $s2,w2
	la $s3,label2
	la $t4,l3
l3:	addi $t3,$t4,12
l4:	sw $t6,12($s2)
	.data
s1:	.space 12
s2:	.space 1000
w1:	.word 1
w2:	.word 2
