# Framework modules
from fastapi.security import HTTPBearer
from fastapi import Depends, HTTPException


reusable_auth = HTTPBearer(scheme_name='Authorization')


class Authorization:

	def __init__(self, auth_key: str):

		self.auth_key = auth_key

	def validate_bear_token(
		self,
		http_authorization_credentials=Depends(reusable_auth)
		):
		"""
			Static method check whether bear token is match
		"""
		if http_authorization_credentials.credentials != self.auth_key:
			raise HTTPException(
					status_code=403,
					detail='Credential is invalid!'
					)
