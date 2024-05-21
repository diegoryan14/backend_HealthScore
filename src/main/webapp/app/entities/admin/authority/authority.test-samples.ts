import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '4db9b792-49cc-4d88-a1ad-3ea98c600908',
};

export const sampleWithPartialData: IAuthority = {
  name: '47a03642-945f-4113-aa11-8d2d0b515088',
};

export const sampleWithFullData: IAuthority = {
  name: '7ff32a53-6a8d-4b9e-9e34-7ae162c78013',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
