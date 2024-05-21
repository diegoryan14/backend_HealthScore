import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 11054,
  login: 'mVzp2@wDi\\Mm\\#x\\qi4\\%1qRtNX\\BdYFlr',
};

export const sampleWithPartialData: IUser = {
  id: 25857,
  login: '_',
};

export const sampleWithFullData: IUser = {
  id: 12186,
  login: 'z@dlF8',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
