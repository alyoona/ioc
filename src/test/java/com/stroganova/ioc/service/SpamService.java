package com.stroganova.ioc.service;

public class SpamService {
    private long spamCount;
    private MailService mailService;

    public long getSpamCount() {
        return spamCount;
    }

    public void setSpamCount(long spamCount) {
        this.spamCount = spamCount;
    }

    public MailService getMailService() {
        return mailService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpamService that = (SpamService) o;

        if (spamCount != that.spamCount) return false;
        return mailService != null ? mailService.equals(that.mailService) : that.mailService == null;
    }

}
