import { defineStore } from 'pinia'

export const useUserInfoStore = defineStore('user-profile', {
  state: () => {
    return {
      user: {},
      token: ''
    }
  },
  actions: {
    setUser(user) {
      this.user = user
    },
    userIsEmpty () {
      return undefined === this.user || null === this.user || {} === this.user
    },
    setToken (token) {
      this.token = token
    },
    tokenIsEmpty () {
      return undefined === this.token || null === this.token || '' === this.token
    }
  }
})