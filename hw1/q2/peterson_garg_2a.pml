/* Q 2a */

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
