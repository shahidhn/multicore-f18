/* Q2 */

/* Peterson's algorithm*/

bool turn, flag[2];
byte ncrit;

active [2] proctype user()
{
	assert(_pid == 0 || _pid == 1);
again:
	flag[_pid] = 1;
	turn = 1 - _pid;
	(!(flag[1 - _pid] == 1 && turn == 1 - _pid));

	ncrit++;
	assert(ncrit == 1);	/* critical section */
	ncrit--;

	flag[_pid] = 0;
	goto again
}

/* Q2a */

// Modified code
bool turn, flag[2];
byte ncrit;

active [2] proctype user()
{
	assert(_pid == 0 || _pid == 1);
again:
	flag[_pid] = 1;
	turn = _pid;
	(!(flag[1 - _pid] == 1 && turn == 1 - _pid));

	ncrit++;
	assert(ncrit == 1);	/* critical section */
	ncrit--;

	flag[_pid] = 0;
	goto again
}

// Trail
-4:-4:-4
1:1:0
2:0:0
3:1:1
4:1:2
5:1:3
6:1:4
7:0:1
8:0:2
9:0:3
10:0:4
11:1:5

// Counter-example
using statement merging
  1:	proc  1 (user:1) peterson_garg_2a.pml:8 (state 1)	[assert(((_pid==0)||(_pid==1)))]
  2:	proc  0 (user:1) peterson_garg_2a.pml:8 (state 1)	[assert(((_pid==0)||(_pid==1)))]
  3:	proc  1 (user:1) peterson_garg_2a.pml:10 (state 2)	[flag[_pid] = 1]
  4:	proc  1 (user:1) peterson_garg_2a.pml:11 (state 3)	[turn = _pid]
  5:	proc  1 (user:1) peterson_garg_2a.pml:12 (state 4)	[(!(((flag[(1-_pid)]==1)&&(turn==(1-_pid)))))]
  6:	proc  1 (user:1) peterson_garg_2a.pml:14 (state 5)	[ncrit = (ncrit+1)]
  7:	proc  0 (user:1) peterson_garg_2a.pml:10 (state 2)	[flag[_pid] = 1]
  8:	proc  0 (user:1) peterson_garg_2a.pml:11 (state 3)	[turn = _pid]
  9:	proc  0 (user:1) peterson_garg_2a.pml:12 (state 4)	[(!(((flag[(1-_pid)]==1)&&(turn==(1-_pid)))))]
 10:	proc  0 (user:1) peterson_garg_2a.pml:14 (state 5)	[ncrit = (ncrit+1)]
spin: peterson_garg_2a.pml:15, Error: assertion violated
spin: text of failed assertion: assert((ncrit==1))
 11:	proc  1 (user:1) peterson_garg_2a.pml:15 (state 6)	[assert((ncrit==1))]
spin: trail ends after 11 steps
#processes: 2
		turn = 0
		flag[0] = 1
		flag[1] = 1
		ncrit = 2
 11:	proc  1 (user:1) peterson_garg_2a.pml:16 (state 7)
 11:	proc  0 (user:1) peterson_garg_2a.pml:15 (state 6)
2 processes created

/* Q2b */

// Modified code
bool turn, flag[2];
byte ncrit;

active [2] proctype user()
{
	assert(_pid == 0 || _pid == 1);
again:
	turn = 1 - _pid;
	flag[_pid] = 1;
	(!(flag[1 - _pid] == 1 && turn == 1 - _pid));

	ncrit++;
	assert(ncrit == 1);	/* critical section */
	ncrit--;

	flag[_pid] = 0;
	goto again
}

// Trail
-4:-4:-4
1:1:0
2:0:0
3:1:1
4:0:1
5:0:2
6:0:3
7:1:2
8:1:3
9:1:4
10:0:4
11:1:5

// Counter-example
using statement merging
  1:	proc  1 (user:1) peterson_garg_2b.pml:8 (state 1)	[assert(((_pid==0)||(_pid==1)))]
  2:	proc  0 (user:1) peterson_garg_2b.pml:8 (state 1)	[assert(((_pid==0)||(_pid==1)))]
  3:	proc  1 (user:1) peterson_garg_2b.pml:10 (state 2)	[turn = (1-_pid)]
  4:	proc  0 (user:1) peterson_garg_2b.pml:10 (state 2)	[turn = (1-_pid)]
  5:	proc  0 (user:1) peterson_garg_2b.pml:11 (state 3)	[flag[_pid] = 1]
  6:	proc  0 (user:1) peterson_garg_2b.pml:12 (state 4)	[(!(((flag[(1-_pid)]==1)&&(turn==(1-_pid)))))]
  7:	proc  1 (user:1) peterson_garg_2b.pml:11 (state 3)	[flag[_pid] = 1]
  8:	proc  1 (user:1) peterson_garg_2b.pml:12 (state 4)	[(!(((flag[(1-_pid)]==1)&&(turn==(1-_pid)))))]
  9:	proc  1 (user:1) peterson_garg_2b.pml:14 (state 5)	[ncrit = (ncrit+1)]
 10:	proc  0 (user:1) peterson_garg_2b.pml:14 (state 5)	[ncrit = (ncrit+1)]
spin: peterson_garg_2b.pml:15, Error: assertion violated
spin: text of failed assertion: assert((ncrit==1))
 11:	proc  1 (user:1) peterson_garg_2b.pml:15 (state 6)	[assert((ncrit==1))]
spin: trail ends after 11 steps
#processes: 2
		turn = 1
		flag[0] = 1
		flag[1] = 1
		ncrit = 2
 11:	proc  1 (user:1) peterson_garg_2b.pml:16 (state 7)
 11:	proc  0 (user:1) peterson_garg_2b.pml:15 (state 6)
2 processes created

