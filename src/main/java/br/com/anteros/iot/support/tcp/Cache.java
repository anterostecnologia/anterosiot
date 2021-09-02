package br.com.anteros.iot.support.tcp;


class Cache {
    private char[] cache = new char[0];

    protected Cache() {
    }

    private synchronized boolean accessCache(char[] data, boolean in) {
        char[] temp;
        if (in) {
            temp = new char[this.cache.length + data.length];
            System.arraycopy(this.cache, 0, temp, 0, this.cache.length);
            System.arraycopy(data, 0, temp, this.cache.length, data.length);
            this.cache = temp;
        } else {
            temp = new char[this.cache.length - data.length];
            System.arraycopy(this.cache, 0, data, 0, data.length);
            System.arraycopy(this.cache, data.length, temp, 0, temp.length);
            this.cache = temp;
        }

        return true;
    }

    protected boolean writeCache(char[] data) {
        try {
            return this.accessCache(data, true);
        } catch (Exception var3) {
            return false;
        }
    }

    protected char[] readCache(int size) {
        try {
            if (size <= this.availableData()) {
                char[] temp = new char[size];
                this.accessCache(temp, false);
                return temp;
            } else {
                return null;
            }
        } catch (Exception var3) {
            return null;
        }
    }

    protected int availableData() {
        try {
            return this.cache.length;
        } catch (Exception var2) {
            return 0;
        }
    }
}

