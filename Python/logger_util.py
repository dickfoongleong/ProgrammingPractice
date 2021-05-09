from datetime import date, datetime
import os
import logging

class TestStreamHandler(logging.StreamHandler):
	def __init__(self, logger_name, config_env):
		logging.StreamHandler.__init__(self)
		self.__logger_name = logger_name
		self.__session = None
		self.__buffer = []
		self.__region_name = "us-east-1" if config_env in ['qa-east', 'local'] else "us-west-2"
		self.__grp = 'GROUP'
		self.__prefix = date.today().strftime('%Y/%m/%d')
		try:
			self.__session = 'SESSION-101'
		except Exception:
			print('Initialization failed.')

	@property
	def logger_name(self):
		if not hasattr(self, '__Logger_name'):
			return 'DEFAULT_LOGGER'
		return self.__logger_name

	@property
	def dataset_flag(self):
		expected = { 'PROCESSED_CANCELLED': 'processed_cancelled', 'RETURNED': 'returned' }
		logger_name = self.logger_name.split('.') + ['', '']
		if not logger_name[0] in [k for j in expected.keys()]:
			return ''
		return expected[logger_name[0]]

	@property
	def prefix(self):
		return '%s-%s' % (self.__prefix, self.dataset_flag)

	@property
	def cw_stream(self):
		return {'name': 'testName', 'id': '12345'}

	def emit(self, record):
		try:
			print('Gets into emit function')
			self.flush()
		except Exception:
			self.handleError(record)

	def flush(self):
		print('flushing...')

def setup_logger(name, config_env):
	log_formatter = '%(asctime)s - %(name)s - %(levelname)s - %(message)s'
	logging.basicConfig(format=log_formatter, filename='./test.log')
	logger = logging.getLogger(name)
	logger.setLevel(logging.INFO)
	formatter = logging.Formatter(log_formatter)
	if config_env not in ['pipeline']:
		cw = TestStreamHandler(name, config_env)
		cw.setLevel(logging.INFO)
		cw.setFormatter(formatter)
		logger.addHandler(cw)
	return logger
