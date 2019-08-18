	.text
main:	nor $t2,$0,$0
	nor $t2,$s2,$0
	nor $t4,$0,$s5
	nor $t6,$t0,$s1
label1:	ori $s4,$t6,0
	ori $s2,$s5,12
	ori $t1,$0,-49
	ori $s1,$s3,-1
	ori $t1,$t5,1
	sll $t5,$0,16
	sll $s6,$t2,0
	sll $s1,$s2,31
	sll $t0,$0,0
label2:	lui $t4,12
	lui $s2,0
	lui $t6,123
	.data
space:	.space 4
