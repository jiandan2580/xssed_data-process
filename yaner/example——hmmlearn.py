#！/usr/bin/env python
#-*- coding:utf-8 -*-
import numpy as np
from hmmlearn import hmm
remodel = hmm.GaussianHMM(n_components=3,covariance_type="full",n_iter=100)
remodel.fit()